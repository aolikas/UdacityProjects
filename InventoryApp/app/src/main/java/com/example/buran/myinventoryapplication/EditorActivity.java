package com.example.buran.myinventoryapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buran.myinventoryapplication.data.InventoryContract.InventoryEntry;
import com.example.buran.myinventoryapplication.data.InventoryDbHelper;

import java.io.File;


/**
 * Allows user to create a new product or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = EditorActivity.class.getSimpleName();

    public static final int PHOTO_REQUEST = 20;

    public static final int EXTERNAL_STORAGE_REQUEST_PERMISSION_CODE = 21;

    private static final int EXISTING_INVENTORY_LOADER = 0;

    //query projection
    public final String[] PRODUCT = {
            InventoryEntry._ID,
            InventoryEntry.COLUMN_NAME,
            InventoryEntry.COLUMN_DESCRIPTION,
            InventoryEntry.COLUMN_PRICE,
            InventoryEntry.COLUMN_QUANTITY,
            InventoryEntry.COLUMN_SOLD_ITEMS,
            InventoryEntry.COLUMN_SUPPLIER_NAME,
            InventoryEntry.COLUMN_IMAGE};

    //Content URI for the existing item(null if it's a new item)
    private Uri mCurrentProductUri;

    //validation variable
    private boolean mItemHasChanged = false;

    private String mCurrentPhotoUri = "no current images";

    private String mOrderEmail;
    private String mOrderItem;
    private int mOrderQuantity = 10;

    //item UI elements
    private EditText mNameEdit;
    private EditText mDescriptionEdit;
    private EditText mPriceEdit;
    private EditText mQuantityEdit;
    private EditText mSoldEdit;
    private EditText mSupplierNameEdit;
    private Button mDecreaseButton;
    private Button mIncreaseButton;
    private Button mImageButton;
    private ImageView mItemImage;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEdit = (EditText) findViewById(R.id.product_name_edit_text);
        mDescriptionEdit = (EditText) findViewById(R.id.product_description_edit_text);
        mPriceEdit = (EditText) findViewById(R.id.product_price_edit_text);
        mQuantityEdit = (EditText) findViewById(R.id.product_quantity_edit_text);
        mSoldEdit = (EditText) findViewById(R.id.product_sales_edit_text);
        mSupplierNameEdit = (EditText) findViewById(R.id.product_supplier_title_edit_text);
        mItemImage = (ImageView) findViewById(R.id.product_image_view);

        mDecreaseButton = (Button) findViewById(R.id.product_quantity_decrease_button);
        mIncreaseButton = (Button) findViewById(R.id.product_quantity_increase_button);
        mImageButton = (Button) findViewById(R.id.button_select_image);

        //monitor activity for protection
        mNameEdit.setOnTouchListener(mTouchListener);
        mDescriptionEdit.setOnTouchListener(mTouchListener);
        mPriceEdit.setOnTouchListener(mTouchListener);
        mQuantityEdit.setOnTouchListener(mTouchListener);
        mSoldEdit.setOnTouchListener(mTouchListener);
        mSupplierNameEdit.setOnTouchListener(mTouchListener);
        mItemImage.setOnTouchListener(mTouchListener);

        //make the button click listener to subtract 1 from quantity
        mDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subtractOneFromQuantity();
            }
        });

        //make the button click listener to add 1 to quantity
        mIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOneToQuantity();
            }
        });

        //Make the button click listener to update image
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhotoProductUpdate(view);
            }
        });


        //examine the intent that was used to launch this activity
        //oin order to figure out if we're creating a new pet or editing an existing one
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            //this is a new item, so change the app bar to say "Add a new product"
            setTitle(getString(R.string.editor_activity_new_item));
        } else {
            //otherwise this is an existing item, so change app bar to say "Edit a product"
            setTitle(getString(R.string.editor_activity_edit_item));

            //initialize a loader to read the inventory data from the database and
            //display the current values in the editor
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }
    }

    //this method add 1 to quantity
    private void addOneToQuantity() {
        String currentQuantityValue = mQuantityEdit.getText().toString();
        int currentValue;
        if (currentQuantityValue.isEmpty()) {
            currentValue = 0;
        } else {
            currentValue = Integer.parseInt(currentQuantityValue);
        }
        mQuantityEdit.setText(String.valueOf(currentValue + 1));
    }

    //this method subtract 1 from quantity
    private void subtractOneFromQuantity() {
        String currentQuantityValue = mQuantityEdit.getText().toString();
        int currentValue;
        if (currentQuantityValue.isEmpty()) {
            return;
        } else if (currentQuantityValue.equals("0")) {
            Toast.makeText(this, R.string.editor_activity_quantity_less_zero, Toast.LENGTH_SHORT).show();
            return;
        } else {
            currentValue = Integer.parseInt(currentQuantityValue);
            mQuantityEdit.setText(String.valueOf(currentValue - 1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //save item to database
                saveItem();
                //exit activity
                finish();
                return true;
            // respond to a click on the "Order more" menu option
            case R.id.action_order_more:
                //call method
                orderItem();
                //exit activity
                finish();
                return true;

            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                //delete item from database
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                //if user didn't make any changes
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                //if user has made some changes
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void onPhotoProductUpdate(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //We are on M or above so we need to ask for runtime permissions
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                invokeGetPhoto();
            } else {
                // we are here if we do not all ready have permissions
                String[] permisionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permisionRequest, EXTERNAL_STORAGE_REQUEST_PERMISSION_CODE);
            }
        } else {
            //We are on an older devices so we dont have to ask for runtime permissions
            invokeGetPhoto();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_REQUEST_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //We got a GO from the user
            invokeGetPhoto();
        } else {
            Toast.makeText(this, R.string.editor_activity_error_stor_permission, Toast.LENGTH_LONG).show();

        }
    }

    private void invokeGetPhoto() {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, PHOTO_REQUEST);
    }

    @Override
    public void onBackPressed() {
        //Go back if we have no changes
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        //otherwise Protect user from loosing info
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                //If we are here, everything processed successfully and we have an Uri data
                Uri mProductPhotoUri = data.getData();
                mCurrentPhotoUri = mProductPhotoUri.toString();
                Log.d(LOG_TAG, "Selected images " + mProductPhotoUri);

                //We use Glide to import photo images
                Glide.with(this).load(mProductPhotoUri)
                        .placeholder(R.mipmap.ic_launcher)
                        .crossFade()
                        .fitCenter()
                        .into(mItemImage);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this,
                mCurrentProductUri,
                PRODUCT,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {

            int i_ID = 0;
            int i_COLUMN_NAME = 1;
            int i_COLUMN_DESCRIPTION = 2;
            int i_COLUMN_PRICE = 3;
            int i_COLUMN_QUANTITY = 4;
            int i_COLUMN_SOLD = 5;
            int i_COLUMN_SUPPLIER = 6;
            int i_COLUMN_IMAGE = 7;

            // Extract values from current cursor
            String name = cursor.getString(i_COLUMN_NAME);
            String description = cursor.getString(i_COLUMN_DESCRIPTION);
            int price = cursor.getInt(i_COLUMN_PRICE);
            int quantity = cursor.getInt(i_COLUMN_QUANTITY);
            int soldItem = cursor.getInt(i_COLUMN_SOLD);
            String supplier = cursor.getString(i_COLUMN_SUPPLIER);
            String image = cursor.getString(i_COLUMN_IMAGE);
            mCurrentPhotoUri = cursor.getString(i_COLUMN_IMAGE);

            mOrderEmail = "order@" + supplier + ".com";
            mOrderItem = name;

            //We updates fields to values on DB
            mNameEdit.setText(name);
            mDescriptionEdit.setText(description);
            mPriceEdit.setText(String.valueOf(price));
            mQuantityEdit.setText(String.valueOf(quantity));
            mSoldEdit.setText(String.valueOf(soldItem));
            mSupplierNameEdit.setText(supplier);

            //Update photo using Glide
            Glide.with(this).load(mCurrentPhotoUri)
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .fitCenter()
                    .into(mItemImage);
        }
    }

    /**
     * Get user input from editor and save/update product into database.
     */
    private void saveItem() {
        //Read Values from text field
        String nameString = mNameEdit.getText().toString().trim();
        String descriptionString = mDescriptionEdit.getText().toString().trim();
        String priceString = mPriceEdit.getText().toString().trim();
        String quantityString = mQuantityEdit.getText().toString().toString();
        String soldString = mSoldEdit.getText().toString().trim();
        String supplierString = mSupplierNameEdit.getText().toString().trim();

        //Check if is new or if an update
        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(descriptionString)
                || TextUtils.isEmpty(priceString) || TextUtils.isEmpty(quantityString)
                || TextUtils.isEmpty(soldString) || TextUtils.isEmpty(supplierString)) {

            Toast.makeText(this, R.string.editor_activity_error_missing_fields, Toast.LENGTH_SHORT).show();
            // No change has been made so we can return
            return;
        }

        //we set values for insert update
        ContentValues contentValues = new ContentValues();

        contentValues.put(InventoryEntry.COLUMN_NAME, nameString);
        contentValues.put(InventoryEntry.COLUMN_DESCRIPTION, descriptionString);
        contentValues.put(InventoryEntry.COLUMN_PRICE, priceString);
        contentValues.put(InventoryEntry.COLUMN_QUANTITY, quantityString);
        contentValues.put(InventoryEntry.COLUMN_SOLD_ITEMS, soldString);
        contentValues.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierString);
        contentValues.put(InventoryEntry.COLUMN_IMAGE, mCurrentPhotoUri);

        if (mCurrentProductUri == null) {

            Uri insertedRow = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);

            if (insertedRow == null) {
                Toast.makeText(this, R.string.editor_activity_error_insert_data, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.editor_activity_success_attempt, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            // We are Updating
            int rowUpdated = getContentResolver().update(mCurrentProductUri, contentValues, null, null);

            if (rowUpdated == 0) {
                Toast.makeText(this, R.string.editor_activity_error_insert_data, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.editor_activity_success_attempt, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEdit.setText("");
        mDescriptionEdit.setText("");
        mPriceEdit.setText("");
        mQuantityEdit.setText("");
        mSoldEdit.setText("");
        mSupplierNameEdit.setText("");

    }

    /**
     * show the the user a dialog to confirm that they want to delete this item.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.editor_activity_delete_dialog_msg);
        builder.setPositiveButton(R.string.editor_activity_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the item.
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.editor_activity_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.editor_activity_dialog_msg);
        builder.setPositiveButton(R.string.editor_activity_discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.editor_activity_keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteItem() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentProductUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, R.string.editor_activity_error_delete, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, R.string.editor_activity_delete_success, Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }

    //Order from supplier
    private void orderItem() {
        String[] TO = {mOrderEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.order_more_subject + mOrderItem);
        emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.order_more_text_part_one + mOrderItem +
                R.string.order_more_text_part_two + mOrderQuantity + R.string.order_more_text_part_three);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
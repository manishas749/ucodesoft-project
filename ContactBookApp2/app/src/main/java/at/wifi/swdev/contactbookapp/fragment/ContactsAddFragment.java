package at.wifi.swdev.contactbookapp.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import at.wifi.swdev.contactbookapp.R;
import at.wifi.swdev.contactbookapp.database.entity.Contact;
import at.wifi.swdev.contactbookapp.databinding.FragmentContactsAddBinding;
import at.wifi.swdev.contactbookapp.viewmodel.ContactViewModel;


public class ContactsAddFragment extends Fragment {

    private FragmentContactsAddBinding binding;
    private Uri imageUri = null;
    private ActivityResultLauncher<Intent> takePhotoLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    public  String IMAGEURI;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactsAddBinding.inflate(inflater, container, false);

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(requireContext(), at.wifi.swdev.contactbookapp.R.array.item_array,
                        android.R.layout.simple_spinner_item);
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categoryOptions.setAdapter(staticAdapter);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContactViewModel contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        activityResultLauncher(requireContext());

        binding.addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                ) {
                    cameraAndGallerySelectorDialog(requireContext());
                } else {
                    Toast.makeText(requireContext(), "Please open settings and allow permissions", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.itemImage.getDrawable() == null)
                {
                    Toast.makeText(requireContext(),"Please select profile picture",Toast.LENGTH_SHORT).show();
                }
                else if (binding.itemName.getText().toString().isEmpty())
                {
                    binding.itemName.setError("Please enter the name");
                }
                else  if (binding.itemNumber.getText().toString().isEmpty())
                {
                    binding.itemNumber.setError("Please enter the number");

                }
                else if (binding.itemAddress.getText().toString().isEmpty())
                {
                    binding.itemAddress.setError("Please enter the address");

                }
                else if (binding.itemEmail.getText().toString().isEmpty())
                {
                    binding.itemEmail.setError("Please enter the email");

                }
                else
                {
                    binding.itemName.setError(null);
                    binding.itemNumber.setError(null);
                    binding.itemAddress.setError(null);
                    binding.itemEmail.setError(null);

                    String name = binding.itemName.getText().toString();
                    String number = binding.itemNumber.getText().toString();
                    String address = binding.itemAddress.getText().toString();
                    String email = binding.itemEmail.getText().toString();
                    String CategoryText = binding.categoryOptions.getSelectedItem().toString();
                    Contact contact = new Contact(IMAGEURI,name,number,address,email,CategoryText);

                    contactViewModel.insert(contact);
                    Navigation.findNavController(view).navigate(R.id.action_contactsAddFragment_to_contactsDisplayFragment);



                }
            }
        });


    }

    private void activityResultLauncher(Context context) {
        takePhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            if (imageUri != null) {

                                binding.itemImage.setImageURI(imageUri);
                                IMAGEURI = saveImage(imageUri, context);
                                Log.d("imageURI", "onActivityResult: "+IMAGEURI);

//                                IMAGEPATH = imageUri.getPath();
//                                Glide.with(context).load(IMAGEPATH).into(binding.itemImage);


                            }


                        }


                    }


                });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // IMAGEPATH = result.getData().getData().getPath();
                            binding.itemImage.setImageURI(result.getData().getData());
                            IMAGEURI = saveImage(result.getData().getData(), context);




                        }


                    }


                });


    }

    public String saveImage(Uri imageUri, Context context) {
        String imagePath = getImagePath(context, imageUri);
        return imagePath;

    }

    public  String getImagePath(Context context, Uri uri) {
        String imagePath = "";
        try {
            InputStream ims = context.getContentResolver().openInputStream(uri);
            int rotation = getRotation(context, uri);
            Bitmap bm = BitmapFactory.decodeStream(ims);

            if (rotation != 0) {
                Matrix matrix = new Matrix();
                switch (rotation) {
                    case 180:
                        matrix.postRotate(180);
                        break;
                    case 270:
                        matrix.postRotate(270);
                        break;
                    default:
                        matrix.postRotate(90);
                        break;
                }

                Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                if (rotatedBitmap != null) {
                    imagePath = saveImageToFolder(rotatedBitmap);
                }
            } else {
                if (bm != null) {
                    imagePath = saveImageToFolder(bm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    private static int getRotation(Context context, Uri uri) {
        // Implement the method to get the rotation of the image from the URI
        return 0;
    }

    private String saveImageToFolder(Bitmap bm) {
        File image = new File(
                getOutputDirectory(), new SimpleDateFormat(
                "yy-MM-dd-HH-mm-ss-SS",
                Locale.getDefault()
        ).format(System.currentTimeMillis()) + ".jpg"
        );

        if (image.exists()) {
            image.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(image);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image.getPath();
    }

    private File getOutputDirectory() {
        String path = Environment.getExternalStorageDirectory().toString() + "/DCIM/.contactBook";
        File direct = new File(path);

        if (!direct.exists()) {
            direct.mkdir();
        }

        return direct;
    }





    public void cameraAndGallerySelectorDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(at.wifi.swdev.contactbookapp.R.layout.image_upload);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        Button takePhotoBtn = dialog.findViewById(at.wifi.swdev.contactbookapp.R.id.takePhotoBtn);
        Button galleryBtn = dialog.findViewById(at.wifi.swdev.contactbookapp.R.id.galleryPhotoBtn);
        Button cancelBtn = dialog.findViewById(at.wifi.swdev.contactbookapp.R.id.cancelBtn);

        takePhotoBtn.setOnClickListener(v -> {
            dialog.dismiss();
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, context.getResources().getString(at.wifi.swdev.contactbookapp.R.string.newPicture));
            values.put(MediaStore.Images.Media.DESCRIPTION, context.getResources().getString(at.wifi.swdev.contactbookapp.R.string.fromYourCamera));
            imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            takePhotoLauncher.launch(cameraIntent);
        });

        galleryBtn.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}

package de.geithonline.imageresizer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.geithonline.imageresizer.utils.BitmapHelper;
import de.geithonline.imageresizer.utils.URIHelper;

public class ImageEditReceiverActivity extends Activity {

    private Button button;
    private String image;
    private ImageView imgView;
    private TextView subjectView;
    private TextView textView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_receiver_activity);
        imgView = (ImageView) findViewById(R.id.imageView1);
        button = (Button) findViewById(R.id.setBackground);
        subjectView = (TextView) findViewById(R.id.subjectView);
        textView = (TextView) findViewById(R.id.textView);

        // Get intent, action and MIME type
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();
        // Check if someone wants to edit an image with us?
        if (Intent.ACTION_EDIT.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                image = handleSendImage(intent); // Handle single image being
                                                 // sent
                Log.i("Receve", "Image = " + image);
                if (image == null) {
                    finish();
                    return;
                } else {
                    // Show image in Activity
                    final Bitmap bmp = BitmapHelper.getCustomImageSampled(image, 1000, 1000);
                    imgView.setImageBitmap(bmp);
                    final String subject = (String) intent.getCharSequenceExtra(Intent.EXTRA_SUBJECT);
                    if (subject != null && !subject.isEmpty()) {
                        subjectView.setText(subject);
                    } else {
                        subjectView.setText("");
                    }
                    final String text = (String) intent.getCharSequenceExtra(Intent.EXTRA_TEXT);
                    if (text != null && !text.isEmpty()) {
                        textView.setText(text);
                    } else {
                        textView.setText("");
                    }
                }
            }
        }
        // Setter Button
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveResizedFile(image);
                finish();
            }
        });

    }

    /**
     * LOading real FilePath from Uri.... adn copiing it to sdcard/data/Folder
     * 
     * @param intent
     * @return
     */
    private String handleSendImage(final Intent intent) {
        final Uri uri = intent.getData();
        if (uri != null) {
            // Update UI to reflect image being shared
            // final String sourceFilename = uri.getPath();
            final String sourceFilename = URIHelper.getPath(this, uri);
            Log.i("SEND_RECEIVED", "SourcePath = " + sourceFilename);
            String outname = "BatteryLWP.jpg";
            if (sourceFilename.endsWith("jpg")) {
                outname = "BatteryLWP.jpg";
            } else if (sourceFilename.endsWith("png")) {
                outname = "BatteryLWP.png";
            } else {
                return null;
            }
            final String myFile = copyfileToMyData(sourceFilename, outname);

            return myFile;

        }
        return null;
    }

    /**
     * Settings prefs to use the image
     * 
     * @param savefile
     */
    private void saveResizedFile(final String savefile) {
        Log.i(this.getClass().getSimpleName(), "ImagePath written to preferences: " + savefile);
    }

    /**
     * Copiing file to myData Dir
     * 
     * @param source
     * @param filename
     * @return
     */
    private String copyfileToMyData(final String source, final String filename) {
        final String destinationDir =
                Environment.getExternalStorageDirectory().getPath() + File.separator + "data" + File.separator + "BatteryLWP" + File.separator;
        final File dir = new File(destinationDir);
        dir.mkdirs();
        final String destinationFilename = destinationDir + filename;

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(source));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            final byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            }
            while (bis.read(buf) != -1);
        } catch (final IOException e) {
            return null;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (final IOException e) {

            }
        }
        return destinationFilename;
    }

}

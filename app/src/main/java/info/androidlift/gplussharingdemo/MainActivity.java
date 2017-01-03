package info.androidlift.gplussharingdemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private static final int REQ_SELECT_PHOTO = 1;
    private static final int REQ_START_SHARE = 2;

    private String TAG = MainActivity.class.getSimpleName();

    private PlusOneButton btnGooglePlusOne;
    private Button btnShareText, btnShareMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGooglePlusOne = (PlusOneButton) findViewById(R.id.btnGooglePlusOne);

        btnShareText = (Button) findViewById(R.id.btnShareText);
        btnShareMedia = (Button) findViewById(R.id.btnShareMedia);
        btnShareText.setOnClickListener(this);
        btnShareMedia.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShareText:
                shareText();
                break;
            case R.id.btnShareMedia:
                shareMedia();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQ_SELECT_PHOTO) {

            if (resultCode == RESULT_OK) {
                Uri selectedImage = intent.getData();
                ContentResolver cr = this.getContentResolver();
                String mime = cr.getType(selectedImage);

                PlusShare.Builder share = new PlusShare.Builder(this);
                share.setText("hello everyone!");
                share.addStream(selectedImage);
                share.setType(mime);
                startActivityForResult(share.getIntent(), REQ_START_SHARE);
            }
        }
    }

    private void shareMedia() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("video/*, image/*");
        startActivityForResult(photoPicker, REQ_SELECT_PHOTO);
    }

    private void shareText() {
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText("Check Trending Google Plus Login Integration Post From Androidlift")
                .setContentUrl(Uri.parse("http://androidlift.info/2016/12/28/android-google-plus-login-integration-signin-example/"))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    protected void onResume() {
        super.onResume();
        // Refresh the state of the +1 button each time the activity receives focus.
        btnGooglePlusOne.initialize(URL, PLUS_ONE_REQUEST_CODE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed : " + connectionResult);
    }
}

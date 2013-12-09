package rlvn.digigraph;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

public class DigiGraphActivity extends Activity {
    private Uri imageUri = null;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(new SignView(this));
        displayMetrics = getResources().getDisplayMetrics();
        Intent intent = getIntent();
        if (Intent.ACTION_MAIN.equals(intent.getAction()))
            setContentView(new SignView(this));

        if (Intent.ACTION_SEND.equals(intent.getAction())) {

            // TODO check uri for null values, invalid values, make sure content
            // is an image, etc
            imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

            setContentView(new PhotoView(this, imageUri, displayMetrics));
            // }
            // if (imageUri != null) {
            // ImageView im = new ImageView(this);
            // im.setImageURI(imageUri);
            // }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        /****
         * Is this the mechanism to extend with filter effects? Intent intent =
         * new Intent(null, getIntent().getData());
         * intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
         * menu.addIntentOptions( Menu.ALTERNATIVE, 0, new ComponentName(this,
         * NotesList.class), null, intent, 0, null);
         *****/
        getMenuInflater().inflate(R.menu.signview_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_clear:
            setContentView(new SignView(this));
            return true;
            // case R.id.menu_done:
            // setContentView(new PhotoView(this));
            // return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}

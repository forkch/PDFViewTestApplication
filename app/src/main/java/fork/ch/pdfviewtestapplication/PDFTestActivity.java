package fork.ch.pdfviewtestapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.joanzapata.pdfview.PDFView;

import org.apache.commons.io.IOUtils;
import org.vudroid.core.DecodeService;
import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecDocument;
import org.vudroid.pdfdroid.codec.PdfContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdftest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            File file = new File(getExternalCacheDir(), "account-circle.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.copy(getAssets().open("account-circle.pdf"), fileOutputStream);
            fileOutputStream.close();
            ImageView iv = (ImageView) findViewById(R.id.imageView);
            Bitmap bitmap = Bitmap.createBitmap(convertDpToPixel(25, this), convertDpToPixel(25, this),
                    Bitmap.Config.ARGB_8888);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                PdfRenderer.Page page = pdfRenderer.openPage(0);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                iv.setImageBitmap(bitmap);

            } else {
                PdfContext pdfContext = new PdfContext();
                CodecDocument codecDocument = pdfContext.openDocument(file.getAbsolutePath());

                Bitmap bitmap1 = codecDocument.getPage(0).renderBitmap(convertDpToPixel(25, this), convertDpToPixel(25, this), new RectF(0, 0, 1, 1));

                Bitmap bitmap2 = Bitmap.createBitmap(convertDpToPixel(25, this), convertDpToPixel(25, this),
                        Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas();
                c.setBitmap(bitmap2);
                c.drawBitmap(bitmap1, 0, 0, new Paint());
//                for(int x = 0; x<bitmap2.getWidth(); x++) {
//                    for(int y = 0; y<bitmap2.getHeight(); y++){
//                        if(bitmap2.getPixel(x, y) == Color.WHITE){
//                            bitmap2.setPixel(x, y, Color.TRANSPARENT);
//                        }
//                    }
//                }

                iv.setImageBitmap(bitmap2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pdftest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

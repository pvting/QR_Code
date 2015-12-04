package com.example.qr_code;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

public class BarcodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        ImageView img = (ImageView)findViewById(R.id.img_only) ;
        TextView tv = (TextView)findViewById(R.id.tv);
//        String str = "6953787364626";
          String str = "1234567890005";
        showBarcode(img, tv, str);
    }

    /**
     * 展示二维码
     * @param img  find ImageView from layout for show barcode
     * @param tv   find textView from layout for show key under barcode
     * @param str  a string of barcode
     */
    private void showBarcode(ImageView img, TextView tv, String str) {
        if(!checkBarcode(str)){
            tv.setText("订单号格式出错");
            return;
        }
        tv.setText(str);
        try {
            Bitmap bm = qr_code(str, BarcodeFormat.EAN_13,300,100,this);
            img.setImageBitmap(bm);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //保持常亮
        keepHighlight();
    }

    /**
     * @param barcode
     */
    private boolean checkBarcode(String barcode){
        if(barcode==null)
            return false;
        else if(barcode.length()!=13)
            return false;
        else{
            char[] cs = barcode.toCharArray();
            for (char c : cs){
                if(c<48||c>58){
                    return false;
                }
            }
            int[] is = new int[cs.length];
            for (int i=0;i<is.length;i++){
                is[i] = cs[i]-48;
            }
            int sum1=0,sum2=0;
            for(int i=0;i<is.length-1;i+=2){
                sum1+=is[i];
                sum2+=is[i+1];
            }
            int sum = sum1 + sum2*3;
            int mol =sum%10;

            int parityBit = mol==0 ? 0 : 10-mol;
            if(parityBit!=is[12]){
                return false;
            }
        }
        return true;
    }

    /**
     * 保持当前页面高亮
     */
    private void keepHighlight() {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = 1.0f;
        window.setAttributes(lp);
    }

    public Bitmap qr_code(String string, BarcodeFormat format,int chang,int kuan,Context context)throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(string, format, DensityUtil.dip2px(context,chang), DensityUtil.dip2px(context,kuan), hst);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}

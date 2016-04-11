package swindroid.suntime.calc;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import swindroid.suntime.R;
import swindroid.suntime.ui.Location;

/**
 * Created by Rohit Narkhede on 10/25/2014.
 */
public class FileReader extends ArrayAdapter<Location> {
    private ArrayList<Location> locations;
    private Context ctx;

    public FileReader(Context context, int textViewResourceId) {
        super(context, textViewResourceId);

        //Store a reference to the Context so we can use it to load a file from Assets.
        this.ctx = context;

        locations = new ArrayList<Location>();
        setLocations();
    }

    public void setLocations() {

        try {
            copyFileToInternalStorage();
           // InputStream in = ctx.getAssets().open("au_locations.txt");
          //  InputStream in =  ctx.getResources().openRawResource(ctx.getResources().getIdentifier("au_locations","raw", ctx.getPackageName()));


            // File sdcard = Environment.getExternalStorageDirectory();
            // File file = new File(sdcard,"au_locations.txt");
            // InputStream in =new FileInputStream(file);
           /*----------------------------------------------*/
          File  outFile = new File(ctx.getFilesDir(),"au_locations.txt");

            InputStream in = new FileInputStream(outFile);
          /*-----------------------------------------------*/

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] RowData = line.split(",");
                Location location = new Location(RowData[0],Double.parseDouble(RowData[1]),Double.parseDouble(RowData[2]),RowData[3]);

                locations.add(location);

            }

        } catch (Exception e) {
             e.printStackTrace();
            Log.i("Error", "file is not there********************");
        }

    }

    public ArrayList<Location> getLocations() {
        return locations;
    }


    public void addLocation(String locationToAdd) throws IOException
    {
        //String value = "Glenmore Park,-33.79068,150.6693,Australia/Sydney";
        String r = "";
        // InputStream inputStream = getResources().openRawResource(R.raw.au_locations);
        //   FileOutputStream fout = new FileOutputStream(inputStream.toString());
        try {
            InputStream inputStream = this.ctx.getResources().openRawResource(R.raw.au_locations);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                r = r + readLine + "\n";
            }
            r = r +"\n"+locationToAdd;

            FileOutputStream fout = new FileOutputStream(inputStream.toString());

            //  BufferedWriter fbw = new BufferedWriter(fout);

            fout.write(r.getBytes());
            //    fbw.newLine();
            //     fbw.append(newLocationDetails);

            fout.close();
        }catch(Exception e)
        {
            Log.d("Exception", "No file found to add");
            e.printStackTrace();
        }




    }


    private void copyFileToInternalStorage() {
        InputStream in = null;
        OutputStream out = null;
        File outFile =  new File(ctx.getFilesDir(),"au_locations.txt");
        if(!outFile.exists()) {
            try {
                in = ctx.getResources().openRawResource(R.raw.au_locations);
                String loc = Environment.getDataDirectory().toString();
                Log.d("TestHTML", "output file" + loc);

                outFile.setWritable(true);
                outFile.setReadable(true);
                Log.d("TestHTML", "output file" + outFile.getAbsolutePath());
                outFile.createNewFile();

                out = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            } catch (IOException e) {
                Log.e("TestHTML", "Failed to copy file", e);
            } finally {
                try {
                    in.close();
                    out.flush();
                    out.close();
                 } catch (Exception e) {
                }
            }
        }
    }
}

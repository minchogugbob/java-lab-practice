import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reverse {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream fos=new FileOutputStream("output.txt");
		OutputStreamWriter osw= new OutputStreamWriter(fos);
		BufferedWriter bw=new BufferedWriter(osw);
		
		InputStream fis=new FileInputStream("input.txt");
		InputStreamReader isr=new InputStreamReader(fis);
		BufferedReader br=new BufferedReader(isr);
		
		String data="";
		ArrayList<String> arr=new ArrayList<String>();
		while(true) {
			if((data=br.readLine())==null) break;
			arr.add(data);		
		}
		
		for(int i=arr.size()-1; i>=0; i--) {
			bw.write(arr.get(i));
			bw.newLine();
		}
		bw.flush();
		br.close();
		isr.close();
		fis.close();
		bw.close();
		osw.close();
		fos.close();
		
	}

}

import com.friend.PhoneNumber;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class TestDriver {

	public static void main(String[] args) throws IOException{

		PhoneNumber myNumber = new PhoneNumber("(203)-736-7606");
		File testFile = null;

		URL url = TestDriver.class.getResource("/files/testFile.dat");

		if(url == null){
			System.err.println("RESOURCE IS NULL");
			System.exit(-1);
		}


		try {
			testFile = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		RandomAccessFile file = new RandomAccessFile(testFile, "rw");

		System.out.printf("Phone Number: %s%n", myNumber);
		pause(2000);
		System.out.println("Writing number to file...");
		myNumber.write(file);
		//System.out.println("Reading file....");

		file.close();

	}

	public static void read(RandomAccessFile file) throws IOException{
		PhoneNumber test = PhoneNumber.read(file);
		pause(2000);
		System.out.printf("Number stored in file: %s", test);
	}

	public static void pause(long milli){
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

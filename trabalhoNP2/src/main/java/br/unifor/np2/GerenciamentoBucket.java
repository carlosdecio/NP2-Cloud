package br.unifor.np2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class GerenciamentoBucket {

	private static GerenciamentoBucket manager;
	private static AmazonS3Client s3;

	public static String BUCKET_NAME = "np2bucket";

	protected GerenciamentoBucket() {
		AWSCredentials credentials = new ProfileCredentialsProvider("cloudnp2").getCredentials();
		s3 = new AmazonS3Client(credentials);
		// s3.setEndpoint("ec2.us-west-2.amazonaws.com");
	}

	public static GerenciamentoBucket manager() {
		if (manager == null) {
			manager = new GerenciamentoBucket();
		}
		return manager;
	}

	public List<S3ObjectSummary> listFiles() {
		ObjectListing ol = s3.listObjects(BUCKET_NAME);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		return objects;
	}

	public File downloadArquivo(String key) {
		File file = null;
		try {
			S3Object o = s3.getObject(BUCKET_NAME, key);
			S3ObjectInputStream s3is = o.getObjectContent();
			file = new File(key);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] read_buf = new byte[1024];
			int read_len = 0;
			while ((read_len = s3is.read(read_buf)) > 0) {
				fos.write(read_buf, 0, read_len);
			}
			s3is.close();
			fos.close();
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			return null;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return null;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
		return file;
	}

	public boolean deleteArquivo(String key) {
		try {
			s3.deleteObject(BUCKET_NAME, key);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			return false;
		}
		return true;
	}

	public boolean uploadArquivo(File file) {
		System.out.println("Upload Arquivo " + file.getAbsolutePath());
		try {
			s3.putObject(BUCKET_NAME, file.getName(), file);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			return false;
		}
		return true;
	}

}

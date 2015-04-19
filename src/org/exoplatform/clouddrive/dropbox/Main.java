package org.exoplatform.clouddrive.dropbox;

import com.dropbox.core.*;

import java.io.*;
import java.util.Locale;

public class Main {

	public static void main(String[] args) throws IOException, DbxException {

        final String APP_KEY = "1qxnqttlbbq2s4x";
        final String APP_SECRET = "eb4unjua39058nb";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
            "JavaTutorial/1.0", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        
        String authorizeUrl = webAuth.start();
      
        System.out.println("1. Go to: " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        // This will fail if the user enters an invalid authorization code.
        DbxAuthFinish authFinish = webAuth.finish(code);
        String accessToken = authFinish.accessToken;

        DbxClient client = new DbxClient(config, accessToken);

        System.out.println("Linked account: " + client.getAccountInfo().displayName);
        System.out.println("User Id: " + client.getAccountInfo().userId);
        
        System.out.println("Country: " + client.getAccountInfo().country);
        System.out.println("referralLink: " + client.getAccountInfo().referralLink);
        System.out.println("Quota: " + client.getAccountInfo().quota.toString());
        System.out.println("Access token: " + accessToken);
        
        // Uploading file
        
        File inputFile = new File("F:/java1/text5.txt");
        FileInputStream inputStream = new FileInputStream(inputFile);
        try {
            DbxEntry.File uploadedFile = client.uploadFile("/magnum-opus.txt",
                DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } finally {
            inputStream.close();
        }
        
        // Listing file
        
        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            System.out.println("	" + child.name + ": " + child.toString());
        }
        
        // Downloading file
        
        FileOutputStream outputStream = new FileOutputStream("F:/java1/magnum-opus(1).txt");
        try {
            DbxEntry.File downloadedFile = client.getFile("/magnum-opus (1).txt", null,
                outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        } finally {
            outputStream.close();
        }	

	}

}

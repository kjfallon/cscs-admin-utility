package edu.syr.eecs.cis.cscs.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Crypto {


    // Takes a String of data and a private key
    // returns a String signature of the data
    public static String sign(String data, PrivateKey privKey) {
        // signature is the result of composite function encryption(hash(data))
        // which is performed by the Signature object
        byte[] dataInBytes = new byte[0];
        try {
            dataInBytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Signature signer = null;
        byte[] signatureInBytes = null;

        try {
            signer = Signature.getInstance("SHA256withRSA");
            signer.initSign(privKey);
            signer.update(dataInBytes);
            signatureInBytes = signer.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return new String( Base64.getEncoder().encodeToString( signatureInBytes ) );
    }

    // Takes a String of data, a String of the provided signature, and the public
    // key of of private key used to sign the data
    public static boolean verify(String data, String signatureInBase64, PublicKey pubKey) {
        // this function decrypts the signature with the public key to get the signature's digest
        // it then compares that to a hash of the data it performs itself,
        // if the values are equal then the signature is verified
        byte[] signatureInBytes = Base64.getDecoder().decode(signatureInBase64);
        byte[] dataInBytes = new byte[0];
        try {
            dataInBytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Signature verifySig = null;
        boolean isVerified = false;
        try {
            verifySig = Signature.getInstance("SHA256withRSA");
            verifySig.initVerify(pubKey);
            verifySig.update(dataInBytes);
            isVerified = verifySig.verify(signatureInBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return isVerified;
    }

    // ref: http://codeartisan.blogspot.com/2009/05/public-key-cryptography-in-java.html
    public static PrivateKey getPrivateKey(String filename)
            throws Exception {

        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // ref: http://codeartisan.blogspot.com/2009/05/public-key-cryptography-in-java.html
    public static PublicKey getPublicKey(String filename)
            throws Exception {

        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }



}

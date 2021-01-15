package com.web.tesdy;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            // internet URL
            // récupérer toutes les urls :
            URL url = new URL("http://digital-vibes.ovh/share/films_serie/series/Camera_Cafe/");
            Reader reader = new InputStreamReader((InputStream) url.getContent());
            new ParserDelegator().parse(reader, new LinkPage(), false);
            // download and save image
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("cat.jpg");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            //close writers
            fos.close();
            rbc.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


class LinkPage extends HTMLEditorKit.ParserCallback {

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        ArrayList<String> listLinks = new ArrayList<String>();
        int compteur = 1;
        if (t == HTML.Tag.A) {
            listLinks.add("http://digital-vibes.ovh/share/films_serie/series/Camera_Cafe/" + a.getAttribute(HTML.Attribute.HREF));

            for (String listLink : listLinks) {
                System.out.println(listLink);
                ReadableByteChannel rbc = null;
                try {
                    rbc = Channels.newChannel(new URL(listLink).openStream());
                    FileOutputStream fos = null;
                    fos = new FileOutputStream(a.getAttribute(HTML.Attribute.HREF).toString()
                            .replaceAll("%20", " ")
                            .replaceAll("%C3%A9", "é")
                            .replaceAll("%27", "'")
                            .replaceAll("%27", "'")
                    );
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    //close writers
                    fos.close();
                    rbc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


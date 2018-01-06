package com.dwidasa.ib.custom;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class AttachmentStreamResponse implements StreamResponse{

	private InputStream is = null;

    /**
     * This can be changed to something obscure, so that it is more likely to trigger a "Save As" dialog, although there
     * is no guarantee.
     * 
     * ex: application/x-download
     * 
     * See http://www.onjava.com/pub/a/onjava/excerpt/jebp_3/index3.html
     */
    protected String contentType = "text/csv";

    protected String extension = "csv";

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	Date date = new Date();
	
    protected String filename = "MutasiRekening";

    public AttachmentStreamResponse(InputStream is, String filenameIn) {
            this.is = is;
            if (filenameIn != null) {
                    this.filename = filenameIn;
            }
    }

    public AttachmentStreamResponse(InputStream is) {
            this.is = is;
    }

    public String getContentType() {
            return contentType;
    }

    public InputStream getStream() throws IOException {
            return is;
    }
    public void prepareResponse(Response arg0) {
            arg0.setHeader("Content-Disposition", "attachment; filename=" + filename + ((extension == null) ? "" : ("." + extension)));
            arg0.setHeader("Expires", "0");
            arg0.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            arg0.setHeader("Pragma", "public");
            //We can't set the length here because we only have an Input Stream at this point. (Although we'd like to.)
            //We can only get size from an output stream.  arg0.setContentLength(.length);
    }

}

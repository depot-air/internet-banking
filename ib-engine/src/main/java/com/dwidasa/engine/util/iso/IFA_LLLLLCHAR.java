package com.dwidasa.engine.util.iso;

import org.jpos.iso.AsciiInterpreter;
import org.jpos.iso.AsciiPrefixer;
import org.jpos.iso.ISOStringFieldPackager;
import org.jpos.iso.NullPadder;

public class IFA_LLLLLCHAR extends ISOStringFieldPackager {

    public IFA_LLLLLCHAR () {
     
        super( NullPadder.INSTANCE, AsciiInterpreter.INSTANCE, AsciiPrefixer.LLLLLL );
    }
    /**
     * @param len - field len
     * @param description symbolic descrption
     */
    public  IFA_LLLLLCHAR( int len, String description ) {
     
        super( len, description, NullPadder.INSTANCE, AsciiInterpreter.INSTANCE, AsciiPrefixer.LLLLLL );

        checkLength( len, 99999 );
    }

    public void setLength( int len ) {

        checkLength( len, 99999 );
        super.setLength( len );
    }

}

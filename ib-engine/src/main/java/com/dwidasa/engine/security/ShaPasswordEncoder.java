package com.dwidasa.engine.security;

import org.springframework.stereotype.Component;

@Component("passwordEncoder")
public class ShaPasswordEncoder extends BasePasswordEncoder {
    /**
     * Initializes the ShaPasswordEncoder for SHA-1 strength
     */
    public ShaPasswordEncoder() {
        this(1);
    }

    /**
     * Initialize the ShaPasswordEncoder with a given SHA stength as supported by the JVM
     * Ex: <code>ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);</code> initializes with SHA-256
     *
     * @param strength Ex: 1, 256, 384, 512
     */
    public ShaPasswordEncoder(int strength) {
        super("SHA-" + strength);
    }
}

package com.dwidasa.engine.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.model.Airline;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.CacheManager;

public class ListUtils {
	private static Logger logger = Logger.getLogger(ListUtils.class);
    
	public static String INDONESIA = "INDONESIA";
	public static List<String> getCountries() {
		List<String> countries = new ArrayList<String>();
        countries.add("AFGHANISTAN");     
        countries.add("ALBANIA");     
        countries.add("ALGERIA");     
        countries.add("ANDORRA");     
        countries.add("ANGOLA");     
        countries.add("ANTIGUA AND BARBUDA");     
        countries.add("ARGENTINA");     
        countries.add("ARMENIA");     
        countries.add("AUSTRALIA");     
        countries.add("AUSTRIA");     
        countries.add("AZERBAIJAN");     
        countries.add("BAHAMAS, THE");     
        countries.add("BAHRAIN");     
        countries.add("BANGLADESH");     
        countries.add("BARBADOS");     
        countries.add("BELARUS");     
        countries.add("BELGIUM");     
//        countries.add("BELIZE");     
        countries.add("BENIN");     
        countries.add("BHUTAN");     
        countries.add("BOLIVIA");     
        countries.add("BOSNIA HERZEGOVINA");     
        countries.add("BOTSWANA");     
        countries.add("BRAZIL");     
        countries.add("BRITAIN");     
        countries.add("BRUNEI");     
        countries.add("BULGARIA");     
        countries.add("BURKINA FASO");     
        countries.add("BURUNDI");     
        countries.add("CAMBODIA");     
        countries.add("CAMEROON");     
        countries.add("CANADA");     
//        countries.add("CAPE VERDE");     
        countries.add("CENTRAL AFRICAN REP");     
        countries.add("CHAD");     
        countries.add("CHILE");     
        countries.add("CHINA");     
        countries.add("COLOMBIA");     
        countries.add("COMOROS");     
        countries.add("CONGO");	//, DEMOCRATIC REPUBLIC OF THE     
        countries.add("COSTA RICA");     
        countries.add("COTE D\'IVOIRE");     
        countries.add("CROATIA");     
        countries.add("CUBA");     
        countries.add("CYPRUS");     
        countries.add("CZECH REPUBLIC");     
        countries.add("CZECHOSLOVAKIA");     
        countries.add("DENMARK");     
//        countries.add("DJIBOUTI");     
        countries.add("DOMINICA");     
        countries.add("DOMINICAN REPUBLIC");     
        countries.add("ECUADOR");     
        countries.add("EGYPT");     
        countries.add("EL SALVADOR");     
        countries.add("ENGLAND");     
        countries.add("EQUATORIAL GUINEA");     
        countries.add("ERITREA");     
        countries.add("ESTONIA");     
        countries.add("ETHIOPIA");     
        countries.add("FIJI");     
        countries.add("FINLAND");     
        countries.add("FRANCE");     
        countries.add("GABON");     
        countries.add("GAMBIA, THE");     
        countries.add("GEORGIA");     
        countries.add("GERMANY");     
        countries.add("GHANA");     
        countries.add("GREAT BRITAIN");     
        countries.add("GREECE");     
        countries.add("GREENLAND");     
        countries.add("GRENADA");     
        countries.add("GUATEMALA");     
        countries.add("GUINEA");     
//        countries.add("GUINEA-BISSAU");     
        countries.add("GUYANA");     
        countries.add("HAITI");     
        countries.add("HONDURAS");     
        countries.add("HUNGARY");     
        countries.add("ICELAND");     
        countries.add("INDIA");     
        countries.add("INDONESIA");     
        countries.add("IRAN");     
        countries.add("IRAQ");     
        countries.add("IRELAND");     
        countries.add("IRELAND, NORTHERN");     
        countries.add("ISRAEL");     
        countries.add("ITALY");     
        countries.add("JAMAICA");     
        countries.add("JAPAN");     
        countries.add("JAVA");     
        countries.add("JORDAN");     
        countries.add("KAZAKHSTAN");     
        countries.add("KENYA");     
//        countries.add("KIRIBATI");     
        countries.add("KOREA, NORTH");     
        countries.add("KOREA, SOUTH");     
        countries.add("KUWAIT");     
        countries.add("KYRGYZSTAN");     
        countries.add("LAOS");     
        countries.add("LATVIA");     
        countries.add("LEBANON");     
        countries.add("LESOTHO");     
        countries.add("LIBERIA");     
        countries.add("LIBYA");     
        countries.add("LIECHTENSTEIN");     
        countries.add("LITHUANIA");     
        countries.add("LUXEMBOURG");     
        countries.add("MACEDONIA");     
        countries.add("MADAGASCAR");     
        countries.add("MALAWI");     
        countries.add("MALAYSIA");     
        countries.add("MALDIVES");     
        countries.add("MALI");     
        countries.add("MALTA");     
        countries.add("MARSHALL ISLANDS");     
        countries.add("MAURITANIA");     
        countries.add("MAURITIUS");     
        countries.add("MEXICO");     
        countries.add("MICRONESIA");     
        countries.add("MOLDOVA");     
        countries.add("MONACO");     
        countries.add("MONGOLIA");     
        countries.add("MOROCCO");     
        countries.add("MOZAMBIQUE");     
        countries.add("MYANMAR (BURMA)");     
        countries.add("NAMIBIA");     
//        countries.add("NAURU");     
        countries.add("NEPAL");     
        countries.add("NETHERLANDS, THE");     
        countries.add("NEW ZEALAND");     
        countries.add("NICARAGUA");     
        countries.add("NIGER");     
        countries.add("NIGERIA");     
//        countries.add("NORTH KOREA");     
        countries.add("NORWAY");     
//        countries.add("NORTHERN IRELAND");     
        countries.add("OMAN");     
        countries.add("PAKISTAN");     
//        countries.add("PALAU");     
        countries.add("PANAMA");     
        countries.add("PAPUA NEW GUINEA");     
        countries.add("PARAGUAY");     
        countries.add("PERU");     
        countries.add("PHILIPPINES, THE");     
        countries.add("POLAND");     
        countries.add("PORTUGAL");     
        countries.add("QATAR");     
        countries.add("ROMANIA");     
        countries.add("RUSSIA");     
        countries.add("RWANDA");     
//        countries.add("ST. KITTS AND NEVIS");     
//        countries.add("ST. LUCIA");     
//        countries.add("ST. VINCENT AND THE GRENADINES");     
        countries.add("SAMOA");     
        countries.add("SAN MARINO");     
//        countries.add("SAO TOME AND PRINCIPE");     
        countries.add("SAUDI ARABIA");     
        countries.add("SCOTLAND");     
        countries.add("SENEGAL");     
//        countries.add("SEYCHELLES");     
        countries.add("SIERRA LEONE");     
        countries.add("SINGAPORE");     
        countries.add("SLOVAKIA");     
        countries.add("SLOVENIA");     
        countries.add("SOLOMON ISLANDS");     
        countries.add("SOMALIA");     
        countries.add("SOUTH AFRICA");     
//        countries.add("SOUTH KOREA");     
        countries.add("SPAIN");     
        countries.add("SRI LANKA");     
        countries.add("SUDAN");     
        countries.add("SURINAME");     
        countries.add("SWAZILAND");     
        countries.add("SWEDEN");     
        countries.add("SWITZERLAND");     
        countries.add("SYRIA");     
        countries.add("TAIWAN");     
        countries.add("TAJIKISTAN");     
        countries.add("TANZANIA");     
        countries.add("THAILAND");     
        countries.add("TOGO");     
        countries.add("TONGA");     
        countries.add("TRINIDAD AND TOBAGO");     
        countries.add("TUNISIA");     
        countries.add("TURKEY");     
        countries.add("TURKMENISTAN");     
//        countries.add("TUVALU");     
        countries.add("UGANDA");     
        countries.add("UKRAINE");     
        countries.add("UNI ARAB EMIRATES");     
        countries.add("UNITED KINGDOM");     
        countries.add("UNITED STATES");     
        countries.add("URUGUAY");     
        countries.add("UZBEKISTAN");     
//        countries.add("VANUATU");     
        countries.add("VATICAN CITY");     
        countries.add("VENEZUELA");     
        countries.add("VIETNAM");     
        countries.add("WALES");     
        countries.add("YEMEN");     
        countries.add("YUGOSLAVIA");     
        countries.add("ZAMBIA");     
        countries.add("ZIMBABWE");
        return countries;
    }


    public static String getLogo(String airlineCode) {
        if (airlineCode.equals("QZ")) return "logoAirasia";   //airasiaa
        else if (airlineCode.equals("Y6")) return "logoBatavia";  //batavia
        else if (airlineCode.equals("QG")) return "logoCitilink";  //citilink
        else if (airlineCode.equals("GA")) return "logoGaruda";  //garuda
        else if (airlineCode.equals("GE")) return "logoGE";  //garuda exekutif
        else if (airlineCode.equals("KD")) return "logoKalstar";  //kalstar
        else if (airlineCode.equals("JT")) return "logoLion";  //lion air
        else if (airlineCode.equals("RI")) return "logoTiger"; //tiger
        else if (airlineCode.equals("MZ")) return "logoMerpati";  //merpati
        else if (airlineCode.equals("SJ")) return "logoSriwijaya";  //sriwijaya

        else if (airlineCode.equals("AK")) return "logoAirasiaMalaysia";
        else if (airlineCode.equals("MV")) return "logoAviaStar";
        else if (airlineCode.equals("ID")) return "logoBatikAir";
        else if (airlineCode.equals("OD")) return "logoMalindo";
        else if (airlineCode.equals("XX")) return "logoNamAir";
        else if (airlineCode.equals("SY")) return "logoSkyAviation";
        else if (airlineCode.equals("SL")) return "logoThaiLion";
        else if (airlineCode.equals("TR")) return "logoTigerAirSing";
        else if (airlineCode.equals("IL")) return "logoTriganaAir";
        else if (airlineCode.equals("IW")) return "logoWingsAir";
        return "logoWingsAir";
    }

}

package se.gareth.swm;

import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Color;
import android.util.Log;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class WorldParser extends DefaultHandler {

    private static final String TAG = WorldParser.class.getName();

    private final GameBase game;
    private ArrayList<WorldDescriptor> mWorldList;
    private WorldDescriptor mWorld;
    private String mTempVal;
    private LevelDescriptor mTempLevel;

    private int mCreaturesAlive;
    private long mCreaturesAliveMinInterval;
    private long mCreaturesAliveMaxInterval;

    public WorldParser(GameBase gameBase) {
        game = gameBase;
        mWorldList = new ArrayList<WorldDescriptor>();
    }

    public ArrayList<WorldDescriptor> getWorldList() {
        return mWorldList;
    }

    /* Start of element handler */
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        mTempVal = "";
        if (qName.equalsIgnoreCase("world")) {
            mWorld = new WorldDescriptor(game, attributes.getValue("name"), mWorldList.size());

            /* reset creatures alive interval values */
            mCreaturesAlive = 5;
            mCreaturesAliveMinInterval = 1000;
            mCreaturesAliveMaxInterval = 3000;

            mTempLevel = null;
            String backgroundName = attributes.getValue("background");
            String backgroundColorString = attributes.getValue("backgroundColor");
            if (backgroundName != null) {
                int backgroundColor = 0xffffffff;
                if (backgroundColorString != null) {
                    try {
                        backgroundColor = Integer.decode(backgroundColorString);
                        backgroundColor = Color.argb(0xff, Color.red(backgroundColor),
                                                     Color.green(backgroundColor), Color.blue(backgroundColor));
                    }
                    catch (NumberFormatException e) {
                        SLog.w(TAG, "Invalid format of background color");
                        backgroundColor = 0xffffffff;
                    }
                }
                mWorld.setBackground(backgroundName, backgroundColor);
            }
        }
        else if (qName.equalsIgnoreCase("level")) {
            if (mWorld != null) {

                String tmpStr;

                String levelTypeStr = attributes.getValue("type");
                LevelDescriptor.Type levelType = LevelDescriptor.Type.Regular;

                if (levelTypeStr != null && levelTypeStr.equalsIgnoreCase("Bonus"))
                    levelType = LevelDescriptor.Type.Bonus;


                tmpStr = attributes.getValue("creaturesAlive");
                if (tmpStr != null) {
                    mCreaturesAlive = Integer.parseInt(tmpStr);
                }
                tmpStr = attributes.getValue("creaturesAliveMinInterval");
                if (tmpStr != null) {
                    mCreaturesAliveMinInterval = Integer.parseInt(tmpStr);
                }
                tmpStr = attributes.getValue("creaturesAliveMaxInterval");
                if (tmpStr != null) {
                    mCreaturesAliveMaxInterval = Integer.parseInt(tmpStr);
                }

                mTempLevel = new LevelDescriptor(levelType, mCreaturesAlive,
                                                 mCreaturesAliveMinInterval, mCreaturesAliveMaxInterval);
            }
        }

        if (mTempLevel != null) {
            if (qName.equalsIgnoreCase("hitable")) {
                int hitableLevel = 1;
                double hitableChance = 1.0;
                int hitableCopies = 1;
                boolean hitableMustBeKilled = true;
                String hitableType = attributes.getValue("type");

                String tmpValue = attributes.getValue("level");
                if (tmpValue != null) {
                    hitableLevel = Integer.parseInt(tmpValue);
                }
                tmpValue = attributes.getValue("chance");
                if (tmpValue != null) {
                    hitableChance = Double.parseDouble(tmpValue);
                }
                tmpValue = attributes.getValue("copies");
                if (tmpValue != null) {
                    hitableCopies = Integer.parseInt(tmpValue);
                }
                tmpValue = attributes.getValue("mustBeKilled");
                if (tmpValue != null && tmpValue.equalsIgnoreCase("false")) {
                    hitableMustBeKilled = false;
                }
                if (hitableType != null) {
                    mTempLevel.addHitable(hitableType, hitableLevel, hitableChance, hitableCopies, hitableMustBeKilled);
                }
            }
        }
    }


    public void characters(char[] ch, int start, int length)
        throws SAXException {
        mTempVal = new String(ch, start, length);
    }


    public void endElement(String uri, String localName, String qName)
        throws SAXException {

        if (qName.equalsIgnoreCase("world")) {
            if (mWorld != null) {
                mWorldList.add(mWorld);
            }
        }
        else if (qName.equalsIgnoreCase("level")) {
            if (mWorld != null) {
                mWorld.addLevelDescriptor(mTempLevel);
            }
        }

        if (mTempLevel != null) {
            if (qName.equalsIgnoreCase("description")) {
                mTempLevel.setDesctiption(mTempVal);
            }
        }

    }


    /*
     * Parse world file into list of world descriptor
     */
    static public ArrayList<WorldDescriptor> parseXML(GameBase gameBase, InputStream worldFile) {

        try {
            /* Start the xml parsing */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            WorldParser xmlParser = new WorldParser(gameBase);
            xr.setContentHandler(xmlParser);
            InputSource inStream = new InputSource();

            inStream.setByteStream(worldFile);
            xr.parse(inStream);

            SLog.w(TAG, "Done");
            return xmlParser.getWorldList();
        }
        catch (Exception e) {
            SLog.pe(TAG, e.getMessage(), e);
            return null;
        }
    }
}



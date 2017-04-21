package client.gui;

import client.resources.ImageLibrary;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class ClientGUI extends JFrame {

    private int mapsize;
    private int sectionWidth = 40;
    private int sectionHeight = 40;
    private byte[] lastAttributes;
    private SectionGUI[] sections;



    public ClientGUI(int mapsize, byte[] stateData) {

        setBackground(new Color(0));
        // set values
        this.mapsize = mapsize;

        // set amount of sections
        sections = new SectionGUI[(int)Math.pow(mapsize,2)];

        // set size of toolbox
        int toolSize = 0;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // impossible to get insets before pack()
        pack();
        Insets insets = getInsets();
        setBounds(100,10,mapsize*sectionWidth+insets.left+ insets.right+toolSize+2, mapsize*sectionHeight+ insets.top+ insets.bottom+2);

        // gives ImageLibrary a reference to this
        ImageLibrary.setClientGUI(this);

        // loads all Images
        ImageLibrary.loadImages();

        Container c = getContentPane();


        for (int id = 0; id < mapsize*mapsize; id++) {
            SectionGUI s = new SectionGUI(id, this);

            // apply stateData
            switch (stateData[id]) {
                case 0: //  NULL
                    s.setIsNull(true);
                    break;
                case 2: // LOCKED
                    s.setLocked(true);
                    break;
            }
            sections[id] = s;
        }

        locateSections();

        for (SectionGUI s:sections) {
            s.reCalcImage();
        }

        for (SectionGUI section : sections) {
            c.add(section);
        }

        setVisible(true);
    }

    public SectionGUI[] getSections() {
        return sections;
    }

    public SectionGUI getSectionById(int id) {
        return sections[id];
    }

    public SectionGUI[] getNeighborsById(int id) {
        SectionGUI[] ret = new SectionGUI[4];

        // nullSection gets returned if the section, which is calling this method,
        // is placed near a border and therefore misses one or more neighbors
        SectionGUI nullSection = new SectionGUI(-1, null);
        nullSection.setIsNull(true);

        ret[0] = (id > mapsize-1)? sections[id-mapsize]:nullSection;
        ret[1] = (id%mapsize != mapsize-1)? sections[id+1]:nullSection;
        ret[2] = (id < Math.pow(mapsize,2)-mapsize)? sections[id+mapsize]:nullSection;
        ret[3] = (id%mapsize != 0)? sections[id-1]:nullSection;

        return ret;
    }

    public void applyDataToSections(byte[] fullData, byte[] units, byte[] attributes) {
        // sets the following data about an SectionGUI: owner, state, colorBits
        setFullData(fullData);

        // sets the following data about an SectionGUI: unitValue
        setUnitData(units);

        // sets the following data about an SectionGUI: attributes
        setAttributeData(attributes);

        try {
            for (SectionGUI s : sections) {
                s.updateImage();
            }
        } catch (Exception e) {
            System.out.println("ERR_UpdateImage");
            e.printStackTrace();
        }
        for (SectionGUI s : sections) {
            s.reCalced = false;
        }
    }

    private void setAttributeData(byte[] attributes) {
        int[] intAttributes = new int[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] < 0)
                intAttributes[i] = attributes[i]+256;
            else
                intAttributes[i] = attributes[i];
        }

        if (attributes != lastAttributes)
            setAttributes(intAttributes);

        lastAttributes = attributes;
    }

    private void setFullData(byte[] fullData) {
        for (int i = 0; i < fullData.length; i++) {
            if (i < Math.pow(mapsize, 2)) { // ownerData
                sections[i].setOwner(fullData[i]);
            } else if (i < 2*Math.pow(mapsize,2)) { //StateData
                switch (fullData[i]) {
                    case 0: // nullField
                        break;
                    case 1:
                        sections[i-(int)Math.pow(mapsize,2)].setLocked(false);
                        break;
                    case 2:
                        sections[i-(int)Math.pow(mapsize,2)].setLocked(true);
                        break;
                }
            } else { // ColorBits
                sections[i-2*(int)Math.pow(mapsize,2)].setColorBits(fullData[i]);
            }
        }
    }

    private void setUnitData(byte[] unitData) {
        for (int i = 0; i < unitData.length; i++) {
            sections[i].setUnitData(unitData[i]);
        }
    }

    private void setAttributes(int[] attributes) {
        try {
            if (attributes.length == 0) return;
            for (int i = 0, sectionId = 0; sectionId < Math.pow(mapsize, 2); i++, sectionId++) {
                sections[sectionId].resetAttributes();
                for (int j = 0, start = i; j < attributes[start]; j++) {
                    i++;
                    sections[sectionId].setAttributeData(attributes[i]);
                }
            }

        }catch (IndexOutOfBoundsException e){
            System.out.println(Arrays.toString(attributes));}
    }

    private void locateSections() {
        for (SectionGUI section : sections) {
            section.setBounds(getRectangle(section.getID()));
        }
    }

    private Rectangle getRectangle(int id) {
        Rectangle ret;
        int x = sectionWidth*(id%mapsize)+1;
        int y = sectionHeight*(id/mapsize)+1;
        ret = new Rectangle(x,y,sectionWidth,sectionHeight);
        return ret;
    }
}

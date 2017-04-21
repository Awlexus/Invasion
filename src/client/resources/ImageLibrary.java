package client.resources;


import client.gui.ClientGUI;
import client.gui.SectionGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ImageLibrary {

    private static ClientGUI clientGUI;


    // stores all path*.png files from resources/ground
    private static BufferedImage[][][][] pathIcons = new BufferedImage[2][2][2][2];


    // stores all *.png files from resources/colors
    private static BufferedImage[][] colorIcons = new BufferedImage[5][5];

    private static BufferedImage[] colorStrength = new BufferedImage[5];

    // completely transparent .png file, applied to specific SectionGUIs as a placeholder
    private static BufferedImage noColor;

    // stores all .gif files from resources/gifs
    // separated by top,right,bot,left - out,in - slow,fast
    private static ImageIcon[][][] speedIcons = new ImageIcon[4][2][2];


    private static ImageIcon GrowthIcon;

    public static void setClientGUI(ClientGUI clientGUI) {
        ImageLibrary.clientGUI = clientGUI;
    }

    public static void loadImages() {
        try {

            /*
              GROUND
              reads all Images of resources/ground/*.png
             */

            //empty
            pathIcons[0][0][0][0] = ImageIO.read(ImageLibrary.class.getResource("ground/path.png"));


            // single
            pathIcons[1][0][0][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTop.png"));
            pathIcons[0][1][0][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathRight.png"));
            pathIcons[0][0][1][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathBot.png"));
            pathIcons[0][0][0][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathLeft.png"));

            // double
            pathIcons[1][1][0][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopRight.png"));
            pathIcons[1][0][1][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopBot.png"));
            pathIcons[1][0][0][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopLeft.png"));
            pathIcons[0][1][1][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathRightBot.png"));
            pathIcons[0][1][0][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathRightLeft.png"));
            pathIcons[0][0][1][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathBotLeft.png"));

            // triple
            pathIcons[1][1][1][0] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopRightBot.png"));
            pathIcons[1][1][0][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopRightLeft.png"));
            pathIcons[1][0][1][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopBotLeft.png"));
            pathIcons[0][1][1][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathRightBotLeft.png"));

            // full
            pathIcons[1][1][1][1] = ImageIO.read(ImageLibrary.class.getResource("ground/pathTopRightBotLeft.png"));

            /*
            COLORS
            reads all images of resources/color/*.png
             */

            // empty field
            noColor = ImageIO.read(ImageLibrary.class.getResource("colors/white.png"));

            // white
            colorIcons[0][0] = ImageIO.read(ImageLibrary.class.getResource("colors/whiteMid.png"));

            // red
            colorIcons[1][0] = ImageIO.read(ImageLibrary.class.getResource("colors/redMid.png"));
            colorIcons[1][1] = ImageIO.read(ImageLibrary.class.getResource("colors/redTop.png"));
            colorIcons[1][2] = ImageIO.read(ImageLibrary.class.getResource("colors/redRight.png"));
            colorIcons[1][3] = ImageIO.read(ImageLibrary.class.getResource("colors/redBot.png"));
            colorIcons[1][4] = ImageIO.read(ImageLibrary.class.getResource("colors/redLeft.png"));

            // green
            colorIcons[2][0] = ImageIO.read(ImageLibrary.class.getResource("colors/greenMid.png"));
            colorIcons[2][1] = ImageIO.read(ImageLibrary.class.getResource("colors/greenTop.png"));
            colorIcons[2][2] = ImageIO.read(ImageLibrary.class.getResource("colors/greenRight.png"));
            colorIcons[2][3] = ImageIO.read(ImageLibrary.class.getResource("colors/greenBot.png"));
            colorIcons[2][4] = ImageIO.read(ImageLibrary.class.getResource("colors/greenLeft.png"));

            // purple
            colorIcons[3][0] = ImageIO.read(ImageLibrary.class.getResource("colors/purpleMid.png"));
            colorIcons[3][1] = ImageIO.read(ImageLibrary.class.getResource("colors/purpleTop.png"));
            colorIcons[3][2] = ImageIO.read(ImageLibrary.class.getResource("colors/purpleRight.png"));
            colorIcons[3][3] = ImageIO.read(ImageLibrary.class.getResource("colors/purpleBot.png"));
            colorIcons[3][4] = ImageIO.read(ImageLibrary.class.getResource("colors/purpleLeft.png"));

            // blue
            colorIcons[4][0] = ImageIO.read(ImageLibrary.class.getResource("colors/blueMid.png"));
            colorIcons[4][1] = ImageIO.read(ImageLibrary.class.getResource("colors/blueTop.png"));
            colorIcons[4][2] = ImageIO.read(ImageLibrary.class.getResource("colors/blueRight.png"));
            colorIcons[4][3] = ImageIO.read(ImageLibrary.class.getResource("colors/blueBot.png"));
            colorIcons[4][4] = ImageIO.read(ImageLibrary.class.getResource("colors/blueLeft.png"));


            // colorStrength
            colorStrength[0] = ImageIO.read(ImageLibrary.class.getResource("colors/white85.png"));
            colorStrength[1] = ImageIO.read(ImageLibrary.class.getResource("colors/white65.png"));
            colorStrength[2] = ImageIO.read(ImageLibrary.class.getResource("colors/white45.png"));
            colorStrength[3] = ImageIO.read(ImageLibrary.class.getResource("colors/white25.png"));
            colorStrength[4] = ImageIO.read(ImageLibrary.class.getResource("colors/white05.png"));

            /*
            SPEED_GIF
            reads all gifs from resources/gifs/*.gif
            [top=0,right=1,bot=2,left=3][out=0,in=1][slow=0,fast=1]
             */

            speedIcons[0][0][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/topOutSlow.gif"));
            speedIcons[0][0][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/topOutFast.gif"));
            speedIcons[0][1][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/topInSlow.gif"));
            speedIcons[0][1][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/topInFast.gif"));

            speedIcons[1][0][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/rightOutSlow.gif"));
            speedIcons[1][0][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/rightOutFast.gif"));
            speedIcons[1][1][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/rightInSlow.gif"));
            speedIcons[1][1][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/rightInFast.gif"));

            speedIcons[2][0][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/botOutSlow.gif"));
            speedIcons[2][0][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/botOutFast.gif"));
            speedIcons[2][1][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/botInSlow.gif"));
            speedIcons[2][1][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/botInFast.gif"));

            speedIcons[3][0][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/leftOutSlow.gif"));
            speedIcons[3][0][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/leftOutFast.gif"));
            speedIcons[3][1][0] = new ImageIcon(ImageLibrary.class.getResource("gifs/leftInSlow.gif"));
            speedIcons[3][1][1] = new ImageIcon(ImageLibrary.class.getResource("gifs/leftInFast.gif"));


            GrowthIcon = new ImageIcon(ImageLibrary.class.getResource("attribute/gold.png"));
        } catch (IOException e) {
            System.out.println("Library:FileRead_ERR");
        }
    }

    public static ImageIcon[] getImage(int id, boolean locked, int owner, int colorBits, boolean[] boolAttributes) {
        ImageIcon[] ret;

        ImageIcon[] basic = getBasicImage(id, locked, owner, colorBits);

        ImageIcon[] attributes = getAttributeImage(boolAttributes);

        List<ImageIcon> listRet= new ArrayList<>();

        if (basic != null)
            for (ImageIcon icon : basic) {
                if (icon != null)
                    listRet.add(icon);
        }

        for (ImageIcon icon : attributes) {
            if (icon != null)
                listRet.add(icon);
        }

        ret = new ImageIcon[listRet.size()];
        listRet.toArray(ret);

        return ret;
    }

    private static ImageIcon[] getBasicImage(int id, boolean locked, int owner, int colorBits) {
        ImageIcon[] ret = new ImageIcon[5];
        BufferedImage pathIcon = getPathImage(id, locked, owner);

        Object[] colorSpeedImage = getColorSpeedImage(id, colorBits);
        if (colorSpeedImage == null)
            return new ImageIcon[] {new ImageIcon(noColor)};

        BufferedImage combined = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(pathIcon,0,0,null);
        g.drawImage(((Image) colorSpeedImage[4]),0,0,null);

        ret[4] = new ImageIcon(combined);


        for (int i = 0; i < 4; i++) {
            if (colorSpeedImage[i] != null)
                ret[i] = ((ImageIcon) colorSpeedImage[i]);
        }
        return ret;
    }

    private static Object[] getColorSpeedImage(int id, int colorBits) {
        Object[] ret = new Object[5];
        int[] pathId = clientGUI.getSectionById(id).getLastPathId();
        boolean[] bits = readBits(colorBits);

        BufferedImage colorImage = getColorImage(id, pathId, bits);
        if (colorImage == null)
            return null;

        ImageIcon[] speedImages = getSpeedImages(pathId, bits);


        System.arraycopy(speedImages, 0, ret, 0, speedImages.length);
        ret[4] = colorImage;

        return ret;
    }

    private static BufferedImage getColorImage(int id, int[] pathId, boolean[] bits) {
        SectionGUI[] neighbors = clientGUI.getNeighborsById(id);
        SectionGUI section = clientGUI.getSectionById(id);
        BufferedImage midImage = getMidImage(id, pathId);
        if (midImage == null)
            return null;

        // gets the different outer images
        BufferedImage topImage = null;
        // if a pathTop exists
        if (pathId[0] == 1)
            // false out, true in
            topImage = (bits[0])? colorIcons[neighbors[0].owner][1] : colorIcons[section.owner][1];

        BufferedImage rightImage = null;
        // if a pathRight exists
        if (pathId[1] == 1)
            // false out, true in
            rightImage = (bits[2])? colorIcons[neighbors[1].owner][2] : colorIcons[section.owner][2];

        BufferedImage botImage = null;
        // if a pathBot exists
        if (pathId[2] == 1)
            // false out, true in
            botImage = (bits[4])? colorIcons[neighbors[2].owner][3] : colorIcons[section.owner][3];

        BufferedImage leftImage = null;
        // if a pathLeft exists
        if (pathId[3] == 1)
            // false out, true in
            leftImage = (bits[6])? colorIcons[neighbors[3].owner][4] : colorIcons[section.owner][4];



        BufferedImage combined = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);

        // merges all images to one image
        Graphics2D g = combined.createGraphics();
        g.drawImage(midImage,0,0,null);
        g.drawImage(topImage,0,0,null);
        g.drawImage(rightImage,0,0,null);
        g.drawImage(botImage,0,0,null);
        g.drawImage(leftImage,0,0,null);
        // releases storage
        g.dispose();

        return combined;
    }

    private static ImageIcon[] getSpeedImages(int[] pathId, boolean[] bits) {
        ImageIcon[] ret = new ImageIcon[4];

        // if a pathTop exists
        if (pathId[0] == 1)
            if (bits[0]) {
                ret[0] = (bits[1]) ? speedIcons[0][1][1] : speedIcons[0][1][0];
            } else
                ret[0] = (bits[1]) ? speedIcons[0][0][1] : speedIcons[0][0][0];

        // if a pathRight exists
        if (pathId[1] == 1)
            if (bits[2]) {
                ret[1] = (bits[3]) ? speedIcons[1][1][1] : speedIcons[1][1][1];
            } else
                ret[1] = (bits[3]) ? speedIcons[1][0][1] : speedIcons[1][0][1];


        // if a pathBot exists
        if (pathId[2] == 1)
            if (bits[4]) {
                ret[2] = (bits[5]) ? speedIcons[2][1][1] : speedIcons[2][1][0];
            } else
                ret[2] = (bits[5]) ? speedIcons[2][0][1] : speedIcons[2][0][0];

        // if a pathLeft exists
        if (pathId[3] == 1)
            if (bits[6]) {
                ret[3] = (bits[7]) ? speedIcons[3][1][1] : speedIcons[3][1][0];
            } else
                ret[3] = (bits[7]) ? speedIcons[3][0][1] : speedIcons[3][0][0];

        return ret;
    }

    /**
     * @return mid part of colored image
     */
    private static BufferedImage getMidImage(int id, int[] pathId) {
        int owner = clientGUI.getSectionById(id).owner;
        if (owner == 0) {
            for (int aPathId : pathId) {
                if (aPathId == 1)
                    return colorIcons[owner][0];
            }
            return  null;
        }

        BufferedImage combined = new BufferedImage(40,40,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combined.createGraphics();
        g.drawImage(colorIcons[owner][0],0,0,null);
        g.drawImage(colorStrength[clientGUI.getSectionById(id).unitValue],0,0,null);
        g.dispose();

        return combined;
    }

    private static ImageIcon[] getAttributeImage(boolean[] attributes) {
        if (attributes.length == 0)
            return new ImageIcon[] {new ImageIcon(noColor)};

        ImageIcon[]ret = new ImageIcon[attributes.length];
        List<ImageIcon> retList = new ArrayList<>();
        if (attributes[0])
            retList.add(GrowthIcon);
        if (attributes[1]) // // TODO: 04.04.2017 PortalIcon
            retList.add(new ImageIcon(noColor));
        retList.toArray(ret);
        return ret;
    }

    private static boolean[] readBits(int colorBits) {
        boolean[] bits = new boolean[8];

        for (int i = 128, index = 0; index < bits.length; i/=2, index++) {
            if (colorBits / i == 1) {
                bits[index] = true;
                colorBits -= i;
            }
        }
        return bits;
    }

    /**
     * @return returns the pathLayout ot the section
     */
    private static BufferedImage getPathImage(int id, boolean isLocked, int owner) {
        SectionGUI[] neighbors = clientGUI.getNeighborsById(id);
        int[] posNeighbor = new int[4];
        if (owner != 0) {
            if (isLocked) {
                posNeighbor[0] = (!neighbors[0].isNull && neighbors[0].owner != 0 &&
                        !neighbors[0].isLocked() && neighbors[0].owner != owner) ? 1 : 0;
                posNeighbor[1] = (!neighbors[1].isNull && neighbors[1].owner != 0 &&
                        !neighbors[1].isLocked() && neighbors[1].owner != owner) ? 1 : 0;
                posNeighbor[2] = (!neighbors[2].isNull && neighbors[2].owner != 0 &&
                        !neighbors[2].isLocked() && neighbors[2].owner != owner) ? 1 : 0;
                posNeighbor[3] = (!neighbors[3].isNull && neighbors[3].owner != 0 &&
                        !neighbors[3].isLocked() && neighbors[3].owner != owner) ? 1 : 0;
            } else {
                posNeighbor[0] = (!neighbors[0].isNull && (!neighbors[0].isLocked() || neighbors[0].owner != owner)) ? 1 : 0;
                posNeighbor[1] = (!neighbors[1].isNull && (!neighbors[1].isLocked() || neighbors[1].owner != owner)) ? 1 : 0;
                posNeighbor[2] = (!neighbors[2].isNull && (!neighbors[2].isLocked() || neighbors[2].owner != owner)) ? 1 : 0;
                posNeighbor[3] = (!neighbors[3].isNull && (!neighbors[3].isLocked() || neighbors[3].owner != owner)) ? 1 : 0;
            }
        } else {
            posNeighbor[0] = (neighbors[0].owner != 0 && !neighbors[0].isLocked()) ? 1 : 0;
            posNeighbor[1] = (neighbors[1].owner != 0 && !neighbors[1].isLocked()) ? 1 : 0;
            posNeighbor[2] = (neighbors[2].owner != 0 && !neighbors[2].isLocked()) ? 1 : 0;
            posNeighbor[3] = (neighbors[3].owner != 0 && !neighbors[3].isLocked()) ? 1 : 0;
        }

        clientGUI.getSectionById(id).setLastPathId(posNeighbor);
        return pathIcons[posNeighbor[0]][posNeighbor[1]][posNeighbor[2]][posNeighbor[3]];
    }
}

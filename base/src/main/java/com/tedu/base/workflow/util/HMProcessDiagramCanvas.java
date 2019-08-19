package com.tedu.base.workflow.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.activiti.image.impl.DefaultProcessDiagramCanvas;
import org.activiti.image.util.ReflectUtil;
import org.apache.log4j.Logger;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/11/20
 */
public class HMProcessDiagramCanvas extends DefaultProcessDiagramCanvas {
	private static final Logger log = Logger.getLogger(HMProcessDiagramCanvas.class);
    public HMProcessDiagramCanvas(int width, int height, int minX, int minY, String imageType, String activityFontName, String labelFontName, String annotationFontName, ClassLoader customClassLoader) {
        super(width, height, minX, minY, imageType, activityFontName, labelFontName, annotationFontName, customClassLoader);
    }


//
//    protected void drawCollapsedTask(String name, GraphicInfo graphicInfo, boolean thickBorder) {
//        // The collapsed marker is now visalized separately
//        drawTask(name, graphicInfo, thickBorder);
//    }
//
//    protected  void  drawTask(String name, GraphicInfo graphicInfo, boolean thickBorder) {
//        Paint originalPaint = g.getPaint();
//        int x = (int) graphicInfo.getX();
//        int y = (int) graphicInfo.getY();
//        int width = (int) graphicInfo.getWidth();
//        int height = (int) graphicInfo.getHeight();
//
//        // Create a new gradient paint for every task box, gradient depends on x and y and is not relative
//        g.setPaint(TASK_BOX_COLOR);
//
//        int arcR = 6;
//        if (thickBorder)
//            arcR = 3;
//
//        // shape
//        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcR, arcR);
//        Rectangle2D rect1=new Rectangle2D.Double(x-16,y-16,16,16);
//
//        g.fill(rect);
//        g.fill(rect1);
//        g.setPaint(TASK_BORDER_COLOR);
//
//        if (thickBorder) {
//            Stroke originalStroke = g.getStroke();
//            g.setStroke(THICK_TASK_BORDER_STROKE);
//            g.draw(rect);
//            g.setColor(Color.blue);
//            g.draw(rect1);
//            g.setStroke(originalStroke);
//        } else {
//            g.draw(rect);
//            g.setColor(Color.blue);
//            g.draw(rect1);
//        }
//
//        g.setPaint(originalPaint);
//        // text
//        if (name != null && name.length() > 0) {
//            int boxWidth = width - (2 * TEXT_PADDING);
//            int boxHeight = height - 16 - ICON_PADDING - ICON_PADDING - MARKER_WIDTH - 2 - 2;
//            int boxX = x + width/2 - boxWidth/2;
//            int boxY = y + height/2 - boxHeight/2 + ICON_PADDING + ICON_PADDING - 2 - 2;
//
//            drawMultilineCentredText(name, boxX, boxY, boxWidth, boxHeight);
//        }
//    }
//
//
//
//    public void drawUserTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(USERTASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawScriptTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(SCRIPTTASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawServiceTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(SERVICETASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawReceiveTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(RECEIVETASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawSendTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(SENDTASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawManualTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(MANUALTASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawBusinessRuleTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(BUSINESS_RULE_TASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawCamelTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(CAMEL_TASK_IMAGE, name, graphicInfo, scaleFactor);
//    }
//
//    public void drawMuleTask(String name, GraphicInfo graphicInfo, double scaleFactor) {
//        drawTask(MULE_TASK_IMAGE, name, graphicInfo, scaleFactor);
//    }


    public void initialize(String imageType) {
        if ("png".equalsIgnoreCase(imageType)) {
            this.processDiagram = new BufferedImage(canvasWidth, canvasHeight,
                    BufferedImage.TYPE_INT_ARGB);
        } else {
            this.processDiagram = new BufferedImage(canvasWidth, canvasHeight,
                    BufferedImage.TYPE_INT_RGB);
        }

        this.g = processDiagram.createGraphics();
        if ("png".equalsIgnoreCase(imageType) == false) {
            this.g.setBackground(new Color(255, 255, 255, 0));
            this.g.clearRect(0, 0, canvasWidth, canvasHeight);
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(Color.black);

        Font font = new Font(activityFontName, Font.BOLD, FONT_SIZE);
        g.setFont(font);
        this.fontMetrics = g.getFontMetrics();

        LABEL_FONT = new Font(labelFontName, Font.ITALIC, 10);
        ANNOTATION_FONT = new Font(annotationFontName, Font.PLAIN, FONT_SIZE);

        try {
            USERTASK_IMAGE = ImageIO.read(ReflectUtil.getResource(
                    "org/activiti/icons/scriptTask.png",customClassLoader));
        } catch (IOException e) {
        	log.error(e.getMessage(),e);
        }

    }

}

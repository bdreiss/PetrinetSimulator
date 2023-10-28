
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.MainFrame;

public class Main {

	public static void main(String[] args) {

		System.setProperty("sun.java2d.uiScale", "1.0");

//		UIManager.put( "control", new Color( 128, 128, 128) );
//		  UIManager.put( "info", new Color(128,128,128) );
//		  UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
//		  UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
//		  UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
//		  UIManager.put( "nimbusFocus", new Color(115,164,209) );
//		  UIManager.put( "nimbusGreen", new Color(176,179,50) );
//		  UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
//		  UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
//		  UIManager.put( "nimbusOrange", new Color(191,98,4) );
//		  UIManager.put( "nimbusRed", new Color(169,46,34) );
//		  UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
//		  UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
//		  UIManager.put( "text", new Color( 230, 230, 230) );
//			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//				if ("Nimbus".equals(info.getName())) {
//					try {
//						javax.swing.UIManager.setLookAndFeel(info.getClassName());
//					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//							| UnsupportedLookAndFeelException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					break;
//				}
//			}
		// Frame erzeugen
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainFrame("Bernd Reiß 3223442");

			}
		});

	}

}

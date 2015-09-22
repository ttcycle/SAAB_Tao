import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * Displays a user-specified splash screen image on startup and hides it when
 * the WorldWindow has finished initialising.
 * <p/>
 * If the application has specified a {@link java.awt.SplashScreen} using the
 * <code>-splash:path/to/image.png</code> on launch, does not create a new
 * splash dialog.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class SplashScreen
{
	private static final URL DEFAULT_IMAGE_URL = SplashScreen.class.getResource("Saab_logo.png");

	private BufferedImage image;
	private JDialog dialog;

	/**
	 * Create a new splash screen with the default splash image
	 */
	public SplashScreen()
	{
		this(null);
	}

	/**
	 * Create a new splash screen with the default splash image as a child of
	 * the provided frame.
	 */
	public SplashScreen(JFrame owner)
	{
		this(owner, DEFAULT_IMAGE_URL);
	}

	/**
	 * Create a new splash screen with the provided image url
	 */
	public SplashScreen(JFrame owner, URL splashImageUrl)
	{
		if (splashScreenSpecifiedAtStartup())
		{
			return;
		}

		dialog = new JDialog(owner);
		initialiseSplashImage(splashImageUrl);
		if (image != null)
		{
			JLabel label = new JLabel(new ImageIcon(image));
			
			dialog.add(label, BorderLayout.CENTER);
			dialog.setUndecorated(true);
			dialog.setBackground(new Color(0,0,0,0));
			dialog.setResizable(false);
			dialog.setSize(image.getWidth(), image.getHeight());
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			dialog.setLayout(new BorderLayout());
		}
	}

	/**
	 * @return Whether the Java 6 splash screen was specified at startup
	 */
	private boolean splashScreenSpecifiedAtStartup()
	{
		return java.awt.SplashScreen.getSplashScreen() != null;
	}

	/**
	 * Initialise the splash image with the image referred to by the provided
	 * URL
	 */
	private void initialiseSplashImage(URL imageUrl)
	{
		if (imageUrl == null)
		{
			return;
		}
		try
		{
			image = ImageIO.read(imageUrl);
		}
		catch (IOException e)
		{
		}
	}

	/**
	 * Hide the splash screen when WorldWindow raises a rendering event.
	 * 
	 * @param wwd
	 *            WorldWindow to listen to
	 */
	public void addRenderingListener(final WorldWindow wwd)
	{
		//hide splash screen when first frame is rendered
		wwd.addRenderingListener(new RenderingListener()
		{
			@Override
			public void stageChanged(RenderingEvent event)
			{
				if (event.getStage() == RenderingEvent.BEFORE_BUFFER_SWAP)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							if (dialog != null)
							{
								dialog.dispose();
							}
						}
					});
					wwd.removeRenderingListener(this);
				}
			}
		});
	}
}
/*
 * Copyright 2012 - 2014 Manuel Laggner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinymediamanager.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinymediamanager.Globals;
import org.tinymediamanager.core.Message.MessageLevel;
import org.tinymediamanager.core.MessageManager;
import org.tinymediamanager.core.TmmModuleManager;
import org.tinymediamanager.core.UpdaterTask;
import org.tinymediamanager.core.Utils;
import org.tinymediamanager.core.threading.TmmTaskManager;
import org.tinymediamanager.ui.components.LightBoxPanel;
import org.tinymediamanager.ui.components.MainTabbedPane;
import org.tinymediamanager.ui.components.TextFieldPopupMenu;
import org.tinymediamanager.ui.components.ToolbarPanel;
import org.tinymediamanager.ui.movies.MovieUIModule;
import org.tinymediamanager.ui.moviesets.MovieSetUIModule;
import org.tinymediamanager.ui.tvshows.TvShowUIModule;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.window.Positions;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jtattoo.plaf.BaseRootPaneUI;

/**
 * The Class MainWindow.
 * 
 * @author Manuel Laggner
 */
public class MainWindow extends JFrame {
  private static final long           serialVersionUID = 5032542783902644134L;
  private static final ResourceBundle BUNDLE           = ResourceBundle.getBundle("messages", new UTF8Control());                   //$NON-NLS-1$
  private final static Logger         LOGGER           = LoggerFactory.getLogger(MainWindow.class);

  public final static Image           LOGO             = Toolkit.getDefaultToolkit().getImage(
                                                           MainWindow.class.getResource("/org/tinymediamanager/ui/images/tmm.png"));

  private static MainWindow           instance;

  private ToolbarPanel                toolbarPanel;
  private JPanel                      rootPanel;
  private JTabbedPane                 tabbedPane;
  private JPanel                      detailPanel;
  private JLayeredPane                layeredPane;

  /** The panel movies. */
  private JPanel                      panelMovies;
  private JPanel                      panelMovieSets;
  private JPanel                      panelTvShows;

  private JPanel                      panelStatusBar;

  private JLabel                      lblLoadingImg;

  private List<String>                messagesList;

  private LightBoxPanel               lightBoxPanel;

  /**
   * Create the application.
   * 
   * @param name
   *          the name
   */
  public MainWindow(String name) {
    super(name);
    setName("mainWindow");
    setIconImage(LOGO);
    setMinimumSize(new Dimension(1100, 700));

    instance = this;
    initialize();

    checkForUpdate();
  }

  /*
   * Initialize the contents of the frame.
   */
  private void initialize() {
    lightBoxPanel = new LightBoxPanel();
    // do nothing, we have our own windowClosing() listener
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    toolbarPanel = new ToolbarPanel();

    // Customize the titlebar. This could only be done if one of the JTattoo look and feels is active. So check this first.
    if (getRootPane().getUI() instanceof BaseRootPaneUI) {
      BaseRootPaneUI rootPaneUI = (BaseRootPaneUI) getRootPane().getUI();
      // Here is the magic. Just add the panel to the titlebar
      rootPaneUI.setTitlePane(getRootPane(), toolbarPanel);
    }
    else {
      // put the toolbar on the top
      getContentPane().add(toolbarPanel, BorderLayout.NORTH);
    }

    layeredPane = new JLayeredPane();
    layeredPane.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("70dlu:grow") }, new RowSpec[] { RowSpec.decode("5dlu"),
        RowSpec.decode("fill:500px:grow") }));
    getContentPane().add(layeredPane);

    rootPanel = new JPanel();
    rootPanel.putClientProperty("class", "rootPanel");
    layeredPane.setLayer(rootPanel, 1);
    layeredPane.add(rootPanel, "1, 1, 1, 2, fill, fill");

    rootPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("12dlu"), ColumnSpec.decode("max(50dlu;default):grow"),
        ColumnSpec.decode("12dlu"), }, new RowSpec[] { RowSpec.decode("fill:500px:grow"), RowSpec.decode("10dlu"), }));

    JSplitPane splitPane = new JSplitPane();
    splitPane.setContinuousLayout(true);
    splitPane.setOpaque(false);
    splitPane.putClientProperty("flatMode", true);
    rootPanel.add(splitPane, "2, 1, fill, fill");

    JPanel leftPanel = new JPanel();
    leftPanel.putClientProperty("class", "roundedPanel");
    leftPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow"), }, new RowSpec[] { RowSpec.decode("fill:default:grow"),
        FormFactory.RELATED_GAP_ROWSPEC, }));
    tabbedPane = new MainTabbedPane();
    leftPanel.add(tabbedPane, "1, 1, fill, fill");
    splitPane.setLeftComponent(leftPanel);

    JPanel rightPanel = new JPanel();
    rightPanel.putClientProperty("class", "roundedPanel");
    rightPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("750px:grow(3)"), }, new RowSpec[] { RowSpec.decode("fill:200px:grow"),
        FormFactory.RELATED_GAP_ROWSPEC, }));
    detailPanel = new JPanel();
    detailPanel.setLayout(new CardLayout(0, 0));
    rightPanel.add(detailPanel, "1, 1, fill, fill");
    splitPane.setRightComponent(rightPanel);

    // to draw the shadow beneath the toolbar
    JPanel shadowPanel = new JPanel() {
      private static final long serialVersionUID = 7962076698737494666L;

      @Override
      public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(32, 32, 32, 80), 0, 4, new Color(0, 0, 0, 0));
        g2.setPaint(gp);
        g2.fill(new Rectangle2D.Double(getX(), getY(), getX() + getWidth(), getY() + getHeight()));
      }
    };
    shadowPanel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("default:grow") }, new RowSpec[] { RowSpec.decode("5dlu") }));
    layeredPane.setLayer(shadowPanel, 2);
    layeredPane.add(shadowPanel, "1, 1, fill, fill");

    addModule(MovieUIModule.getInstance());
    toolbarPanel.setUIModule(MovieUIModule.getInstance());
    addModule(MovieSetUIModule.getInstance());
    addModule(TvShowUIModule.getInstance());

    ChangeListener changeListener = new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent changeEvent) {
        JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
        if (sourceTabbedPane.getSelectedComponent() instanceof ITmmTabItem) {
          ITmmTabItem activeTab = (ITmmTabItem) sourceTabbedPane.getSelectedComponent();
          toolbarPanel.setUIModule(activeTab.getUIModule());
          CardLayout cl = (CardLayout) detailPanel.getLayout();
          cl.show(detailPanel, activeTab.getUIModule().getModuleId());
        }
      }
    };
    tabbedPane.addChangeListener(changeListener);

    // message panel
    MessageManager.instance.addListener(new UIMessageListener());

    // shutdown listener - to clean database connections safely
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        closeTmm();
      }
    });
    MessageManager.instance.addListener(new UIMessageListener());

    // mouse event listener for context menu
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      @Override
      public void eventDispatched(AWTEvent arg0) {
        if (arg0 instanceof MouseEvent && MouseEvent.MOUSE_RELEASED == arg0.getID() && arg0.getSource() instanceof JTextComponent) {
          MouseEvent me = (MouseEvent) arg0;
          JTextComponent tc = (JTextComponent) arg0.getSource();
          if (me.isPopupTrigger() && tc.getComponentPopupMenu() == null) {
            TextFieldPopupMenu.buildCutCopyPaste().show(tc, me.getX(), me.getY());
          }
        }
      }
    }, AWTEvent.MOUSE_EVENT_MASK);
  }

  private void addModule(ITmmUIModule module) {
    tabbedPane.addTab(module.getTabTitle(), module.getTabPanel());
    detailPanel.add(module.getDetailPanel(), module.getModuleId());
  }

  public void closeTmm() {
    closeTmmAndStart(null);
  }

  public void closeTmmAndStart(ProcessBuilder pb) {
    int confirm = JOptionPane.YES_OPTION;
    // if there are some threads running, display exit confirmation
    if (TmmTaskManager.getInstance().poolRunning()) {
      confirm = JOptionPane.showOptionDialog(null, BUNDLE.getString("tmm.exit.runningtasks"), BUNDLE.getString("tmm.exit.confirmation"),
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null); //$NON-NLS-1$
    }
    if (confirm == JOptionPane.YES_OPTION) {
      LOGGER.info("bye bye");
      try {
        // send shutdown signal
        TmmTaskManager.getInstance().shutdown();
        // save unsaved settings
        Globals.settings.saveSettings();
        // hard kill
        TmmTaskManager.getInstance().shutdownNow();
        // close database connection
        TmmModuleManager.getInstance().shutDown();
        // clear cache directory
        if (Globals.settings.isClearCacheShutdown()) {
          File cache = new File("cache" + File.separator + "url");
          if (cache.exists()) {
            FileUtils.deleteDirectory(cache);
          }
        }
      }
      catch (Exception ex) {
        LOGGER.warn("", ex);
      }
      dispose();

      // spawn our process
      if (pb != null) {
        try {
          LOGGER.info("Going to execute: " + pb.command());
          pb.start();
        }
        catch (IOException e) {
          LOGGER.error("Cannot spawn process:", e);
        }
      }

      System.exit(0); // calling the method is a must
    }
  }

  private void checkForUpdate() {
    try {
      final SwingWorker<Boolean, Void> updateWorker = new UpdaterTask();

      updateWorker.addPropertyChangeListener(new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
          if ("state".equals(evt.getPropertyName()) && evt.getNewValue() == StateValue.DONE) {
            try {
              boolean update = updateWorker.get();
              LOGGER.debug("update result was: " + update);
              if (update) {
                int answer = JOptionPane.showConfirmDialog(null, BUNDLE.getString("tmm.update.message"), BUNDLE.getString("tmm.update.title"),
                    JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.OK_OPTION) {
                  LOGGER.info("Updating...");

                  // spawn getdown and exit TMM
                  closeTmmAndStart(Utils.getPBforTMMupdate());
                }
              }
            }
            catch (Exception e) {
              LOGGER.error("Update task failed!" + e.getMessage());
            }
          }
        }
      });

      // update task start a few secs after GUI...
      Timer timer = new Timer(5000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          updateWorker.execute();
        }
      });
      timer.setRepeats(false);
      timer.start();
    }
    catch (Exception e) {
      LOGGER.error("Update task failed!" + e.getMessage());
    }
  }

  public static MainWindow getActiveInstance() {
    return instance;
  }

  /**
   * Gets the frame.
   * 
   * @return the frame
   */
  public static JFrame getFrame() {
    return instance;
  }

  public void addMessage(String title, String message) {
    if (Globals.settings.isShowNotifications()) {
      NotificationBuilder builder = new NotificationBuilder().withMessage(message).withTitle(title).withStyle(new TmmNotificationStyle())
          .withPosition(Positions.SOUTH_EAST);
      builder.showNotification();
    }
  }

  public void addMessage(MessageLevel level, String title, String message) {
    if (Globals.settings.isShowNotifications()) {
      NotificationBuilder builder = new NotificationBuilder().withMessage(message).withTitle(title).withStyle(new TmmNotificationStyle())
          .withPosition(Positions.SOUTH_EAST).withIcon(IconManager.ERROR);
      builder.showNotification();
    }

    if (messagesList != null) {
      messagesList.add(message + ": " + title);
    }
  }

  public void createLightbox(String pathToFile, String urlToFile) {
    lightBoxPanel.setImageLocation(pathToFile, urlToFile);
    lightBoxPanel.showLightBox(instance);
  }
}

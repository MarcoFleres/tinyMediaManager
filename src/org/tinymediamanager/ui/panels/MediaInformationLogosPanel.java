/*
 * Copyright 2012 - 2015 Manuel Laggner
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
package org.tinymediamanager.ui.panels;

import org.tinymediamanager.core.IMediaInformation;
import org.tinymediamanager.ui.converter.CertificationImageConverter;
import org.tinymediamanager.ui.converter.MediaInfoAspectRatioConverter;
import org.tinymediamanager.ui.converter.MediaInfoAudioChannelsConverter;
import org.tinymediamanager.ui.converter.MediaInfoAudioCodecConverter;
import org.tinymediamanager.ui.converter.MediaInfoVideoCodecConverter;
import org.tinymediamanager.ui.converter.MediaInfoVideoFormatConverter;

import javax.swing.*;
import java.awt.*;

/**
 * The class MediaInformationLogosPanel is used to display all media info related logos
 */
public class MediaInformationLogosPanel extends JPanel {

  private IMediaInformation               mediaInformationSource;

  private CertificationImageConverter     certificationImageConverter     = new CertificationImageConverter();
  private MediaInfoVideoFormatConverter   mediaInfoVideoFormatConverter   = new MediaInfoVideoFormatConverter();
  private MediaInfoAspectRatioConverter   mediaInfoAspectRatioConverter   = new MediaInfoAspectRatioConverter();
  private MediaInfoVideoCodecConverter    mediaInfoVideoCodecConverter    = new MediaInfoVideoCodecConverter();
  private MediaInfoAudioCodecConverter    mediaInfoAudioCodecConverter    = new MediaInfoAudioCodecConverter();
  private MediaInfoAudioChannelsConverter mediaInfoAudioChannelsConverter = new MediaInfoAudioChannelsConverter();

  private JLabel                          lblCertification                = new JLabel();
  private JLabel                          lblVideoFormat                  = new JLabel();
  private JLabel                          lblAspectRatio                  = new JLabel();
  private JLabel                          lblVideoCodec                   = new JLabel();
  private JLabel                          lblVideo3d                      = new JLabel();
  private JLabel                          lblAudioCodec                   = new JLabel();
  private JLabel                          lblAudioChannels                = new JLabel();

  public MediaInformationLogosPanel() {
    setLayout(new FlowLayout());

    add(lblCertification);
    add(lblVideoFormat);
    add(lblAspectRatio);
    add(lblVideoCodec);
    add(lblVideo3d);
    add(lblAudioCodec);
    add(lblAudioChannels);
  }

  public void setMediaInformationSource(IMediaInformation source) {
    this.mediaInformationSource = source;

    lblCertification.setIcon(certificationImageConverter.convertForward(source.getCertification()));
    lblVideoFormat.setIcon(mediaInfoVideoFormatConverter.convertForward(source.getMediaInfoVideoFormat()));
    lblAspectRatio.setIcon(mediaInfoAspectRatioConverter.convertForward(source.getMediaInfoAspectRatio()));
    lblVideoCodec.setIcon(mediaInfoVideoCodecConverter.convertForward(source.getMediaInfoVideoCodec()));
    // ToDo add 3d logo
    lblAudioCodec.setIcon(mediaInfoAudioCodecConverter.convertForward(source.getMediaInfoAudioCodec()));
    lblAudioChannels.setIcon(mediaInfoAudioChannelsConverter.convertForward(source.getMediaInfoAudioChannels()));
  }
}

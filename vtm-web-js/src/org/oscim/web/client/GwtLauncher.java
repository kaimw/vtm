/*
 * Copyright 2013 Hannes Janetzek
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.oscim.web.client;

import org.oscim.core.Tile;
import org.oscim.gdx.client.MapConfig;
import org.timepedia.exporter.client.ExporterUtil;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderCallback;
import com.badlogic.gdx.backends.gwt.preloader.Preloader.PreloaderState;

public class GwtLauncher extends GwtApplication {

	@Override
	public void onModuleLoad() {
		//GWT.create(GwtGdxMap.class);
		//JsOverlays.init();
		ExporterUtil.exportAll();

		super.onModuleLoad();
	}

	@Override
	public GwtApplicationConfiguration getConfig() {

		GwtApplicationConfiguration cfg =
		        new GwtApplicationConfiguration(getWindowWidth(),
		                                        getWindowHeight());

		cfg.canvasId = "map-canvas";
		cfg.stencil = true;
		cfg.fps = 120;

		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener() {
		Tile.SIZE = MapConfig.get().getTileSize();

		return new GwtMap();
	}

	@Override
	public PreloaderCallback getPreloaderCallback() {
		return new PreloaderCallback() {

			@Override
			public void update(PreloaderState state) {
			}

			@Override
			public void error(String file) {
				//log.debug("error loading " + file);
			}
		};
	}

	private static native int getWindowWidth() /*-{
		return $wnd.innerWidth;
	}-*/;

	private static native int getWindowHeight() /*-{
		return $wnd.innerHeight;
	}-*/;

}

package org.oscim.test;

import static org.oscim.utils.ColorUtil.saturate;

import org.oscim.gdx.GdxMap;
import org.oscim.gdx.GdxMapApp;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.renderer.MapRenderer;
import org.oscim.theme.RenderTheme;
import org.oscim.theme.VtmThemes;
import org.oscim.theme.rule.Rule;
import org.oscim.theme.rule.Rule.RuleVisitor;
import org.oscim.theme.styles.AreaStyle;
import org.oscim.theme.styles.AreaStyle.AreaBuilder;
import org.oscim.theme.styles.LineStyle;
import org.oscim.theme.styles.LineStyle.LineBuilder;
import org.oscim.theme.styles.RenderStyle;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input;

public class RuleVisitorTest extends GdxMap {

	final Logger log = LoggerFactory.getLogger(RuleVisitorTest.class);
	RenderTheme mTheme;
	double mSaturation = 1;

	@Override
	protected boolean onKeyDown(int keycode) {
		VectorTileLayer l = (VectorTileLayer) mMap.layers().get(1);
		RenderTheme t = (RenderTheme) l.getTheme();

		if (keycode == Input.Keys.NUM_1) {
			mSaturation += 0.1;
			t.traverseRules(new SaturateLineStyles(mSaturation, true, true, true));
			t.updateStyles();
			mMap.render();
			return true;
		}
		if (keycode == Input.Keys.NUM_2) {
			mSaturation -= 0.1;
			t.traverseRules(new SaturateLineStyles(mSaturation, true, true, true));
			t.updateStyles();
			mMap.render();
			return true;
		}

		return super.onKeyDown(keycode);
	}

	@Override
	public void createLayers() {
		MapRenderer.setBackgroundColor(0xf0f0f0);

		TileSource ts = new OSciMap4TileSource();
		VectorTileLayer l = mMap.setBaseMap(ts);

		mMap.setTheme(VtmThemes.DEFAULT);
		RenderTheme t = (RenderTheme) l.getTheme();
		mTheme = t;
		//t.traverseRules(new DesaturateAreaStyles());
		//t.traverseRules(new DesaturateLineStyles());
		t.traverseRules(new SaturateLineStyles(0.5, true, true, true));
		t.updateStyles();

		//mMap.setMapPosition(7.707, 81.689, 1 << 16);

		mMap.setMapPosition(53.08, 8.82, 1 << 16);
	}

	public static void main(String[] args) {
		GdxMapApp.init();
		GdxMapApp.run(new RuleVisitorTest(), null, 400);
	}

	static class SaturateLineStyles extends RuleVisitor {
		private final LineBuilder<?> lineBuilder = LineStyle.builder();
		private final AreaBuilder<?> areaBuilder = AreaStyle.builder();

		private final double saturation;
		private final boolean modifyArea;
		private final boolean modifyLine;
		private final boolean relative;

		public SaturateLineStyles(double saturation, boolean relative, boolean modArea,
		        boolean modLine) {
			this.saturation = saturation;
			this.modifyArea = modArea;
			this.modifyLine = modLine;
			this.relative = relative;
		}

		@Override
		public void apply(Rule r) {
			for (RenderStyle style : r.styles) {

				if (modifyLine && style instanceof LineStyle) {
					LineStyle s = (LineStyle) style;

					s.set(lineBuilder.set(s)
					    .color(saturate(s.color, saturation, relative))
					    .stippleColor(saturate(s.stippleColor, saturation, relative))
					    .build());
					continue;
				}

				if (modifyArea && style instanceof AreaStyle) {
					AreaStyle s = (AreaStyle) style;

					s.set(areaBuilder.set(s)
					    .color(saturate(s.color, saturation, relative))
					    .blendColor(saturate(s.blendColor, saturation, relative))
					    .build());
				}
			}

			super.apply(r);
		}
	}
}


import java.io.FileWriter;

import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;


public class ImageTable {

	public static void main(String[] args) {
		try {
			FileWriter out = new FileWriter("/Users/bjorn/Desktop/test.html");
			BufferedImage image = ImageIO.read(new File("/Users/bjorn/Pictures/widescreen.png"));
			out.write("<html><head><style>td { width:1px; height:1px;}</style></head><body>");
			out.write("<table border=0 cellspacing=0 cellpadding=0 width=" + image.getWidth() + " height=" + image.getHeight() + ">");

			boolean[][] spanned = new boolean[image.getWidth()][image.getHeight()]; 

			for (int y = 0; y < image.getHeight(); y++) {
				out.write("<tr>");
				for (int x = 0; x < image.getWidth(); x++) {
					if (!spanned[x][y]) {
						out.write("<td style='background:#");
						int rgb = image.getRGB(x, y);
						String hex = Integer.toHexString(0xffffff & rgb);
						out.write("00000".substring(hex.length() - 1));
						out.write(hex);
						out.write("'");
						int colspan = 1;
						int rowspan = 1;
						boolean maxColspan = false;
						boolean maxRowspan = false;
						while(!maxColspan && !maxRowspan) {
							
							if (!maxColspan) {
								colspan++;
								if (colspan > 900) {
									maxColspan = true;
								} else {
									for (int span = 0; span < rowspan; span++) {
										int dx = x + colspan - 1;
										int dy = y + span;
										if (spanned[dx][dy] || image.getRGB(dx, dy) != rgb) {
											maxColspan = true;
											break;
										}
									}
								}
							}

							if (!maxRowspan) {
								rowspan++;
								if (rowspan > 900) {
									maxRowspan = true;
								} else {
									for (int span = 0; span < colspan; span++) {
										int dx = x + span;
										int dy = y + rowspan - 1;
										if (spanned[dx][dy] || image.getRGB(dx, dy) != rgb) {
											maxRowspan = true;
											break;
										}
									}
								}
							}
									
							if (!maxColspan) {
								for (int span = 0; span < rowspan; span++) {
									spanned[x + colspan - 1][y + span] = true;
								}
							}

							if (!maxRowspan) {
								for (int span = 0; span < rowspan; span++) {
									spanned[x + span][y + rowspan - 1] = true;
								}
							}
						}
						
						if (colspan > 1) {
							out.write(" colspan=" + colspan);
							x += colspan - 1;
						}
						if (rowspan > 1) {
							out.write(" rowspan=" + rowspan);
						}
						out.write("></td>");
					}
				}
				out.write("</tr>\n");
			}
			out.write("</table></body></html>");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

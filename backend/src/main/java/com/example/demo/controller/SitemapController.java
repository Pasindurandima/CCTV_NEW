package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@RestController
@CrossOrigin
public class SitemapController {

    private static final List<String> STATIC_PATHS = List.of("/", "/store", "/about", "/contact");

    private final ProductRepository productRepository;
    private final String siteUrl;

    public SitemapController(
            ProductRepository productRepository,
            @Value("${SITEMAP_SITE_URL:}") String sitemapSiteUrl,
            @Value("${VITE_SITE_URL:}") String viteSiteUrl) {
        this.productRepository = productRepository;
        if (sitemapSiteUrl != null && !sitemapSiteUrl.isBlank()) {
            this.siteUrl = sitemapSiteUrl;
        } else if (viteSiteUrl != null && !viteSiteUrl.isBlank()) {
            this.siteUrl = viteSiteUrl;
        } else {
            this.siteUrl = "https://secuengineering.netlify.app";
        }
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getSitemap() {
        List<Product> products = productRepository.findAll();
        String sitemapXml = buildSitemapXml(products);
        return ResponseEntity.ok(sitemapXml);
    }

    private String buildSitemapXml(List<Product> products) {
        String today = LocalDate.now().toString();
        StringBuilder sitemap = new StringBuilder(1024);

        sitemap.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sitemap.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        for (String path : STATIC_PATHS) {
            sitemap.append(buildUrlEntry(siteUrl + path, today, "monthly", "0.8"));
        }

        for (Product product : products) {
            String slug = product.getSlug();
            if (slug == null || slug.isBlank()) {
                slug = String.valueOf(product.getId());
            }
            sitemap.append(buildUrlEntry(siteUrl + "/product/" + slug, today, "weekly", "0.8"));
        }

        sitemap.append("</urlset>\n");
        return sitemap.toString();
    }

    private String buildUrlEntry(String loc, String lastmod, String changefreq, String priority) {
        return "  <url>\n"
                + "    <loc>" + escapeXml(loc) + "</loc>\n"
                + "    <lastmod>" + escapeXml(lastmod) + "</lastmod>\n"
                + "    <changefreq>" + escapeXml(changefreq) + "</changefreq>\n"
                + "    <priority>" + escapeXml(priority) + "</priority>\n"
                + "  </url>\n";
    }

    private String escapeXml(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}

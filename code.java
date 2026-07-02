// decoder.js - un singur fisier, distribuit/cache-uit o singura data,
// folosit de TOATE cele 1000 de pagini generate. Nu se dubleaza per pagina.
(async function () {
    const tag = document.getElementById('p');
    const b64 = tag.textContent;

    // base64 -> bytes
    const binary = Uint8Array.from(atob(b64), c => c.charCodeAt(0));

    // decompresie nativa, fara librarii externe (Chrome 80+, Firefox 113+, Safari 16.4+)
    const ds = new DecompressionStream('deflate-raw');
    const stream = new Blob([binary]).stream().pipeThrough(ds);
    const html = await new Response(stream).text();

    document.open();
    document.write(html);
    document.close();
})();



<!DOCTYPE html><html><head><meta charset="utf-8"></head><body>
<script src="decoder.js"></script>
<script id="p" type="application/octet-stream+b64">pVTLbtswELzrK7a+9GKLTY6JQsB5tA3QoEFgIOiRkmiTMEUKJBVV+aP+Rr+su6TkNijQS0+myN3Zmd1ZVyp2hhfVu80GdvJ7hFrGUUoLwh6MhNqL5ihjAB3wBj7vHr5AFAc8t3RlXYRWh96ISbZl8eBCpOewhjA0CkSAqGTOogy25NO9iBjkvRvwgaIaZ6O0WMrtCwG9OMg13nUStMUv7cMlBPrM+EYfEfhpDXvnQYByXr8igDDgB4OZIVLBQhhnZQk3rusI+y2vSHonN7z3ErwUrbaHNQj8eCMLRiVtQfHPsk68SHlQbrQl7BTxQw6diNrZU/eS7Lvt7SybTkk2ZhZvm/ZPiOuvt98yRDotEL+zNxucnULu+BN1NJLfYRepI+lr/YcM7HcS7XrscDqO2rZuLCuWM4uKzUjJDP9Nq3btROzOZk6U2Qmcplp6PYRBGDOllyBwtstgiA/ywtSi6vm1hKrmtTNtxWpOfsDpRkSg4Xk4ygl6p3G8JTwOSWVHQQJNEuIFVKxHmIFcbjQnXXt0UwQdc1wCodCK4fspKEh0ZJuiLqHSXKO5dKNfZcU0T0VH59swJ7GE3/P7rvfuRWZQ3ZFb6gmrNGYgzbRE6bacafW80t0Bgm+uVirG/oKxcRzLbjrofSjR/+wG4fx9ysG7FQgTr1Zb+ISOjXm1nmTAao1c8QVz27ZJvj3ivDOXvXjBHYnYStwWL/e5XJjrtUPXaZkLrjgZPWBsxQQvi2usdIShzzhpAUYd1d9bB7iKcXSLNOUTl4/a0pDXJzonAgR1XtIf0IoL3AolMzyVPQ0G1wwWPmUGJntucTQiqWxcP3l9UJHWVjcym6/nP3/AszY4psehxumq5LjzD2dnGYTN9mT5D/AX</script>
</body></html>


    import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Comprima un fisier HTML (deflate raw, fara header zlib/gzip - cel mai mic
 * overhead posibil) si il embedeaza base64 intr-o pagina "shell" minimala
 * care se auto-decodeaza in browser folosind Compression Streams API
 * (DecompressionStream), disponibila nativ, fara nicio librarie JS externa.
 *
 * Foloseste "raw deflate" (nowrap = true) pt ca in JS ii corespunde
 * new DecompressionStream('deflate-raw') - fara cei ~18 octeti de header/footer gzip.
 */
public class HtmlPacker {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Usage:");
            System.err.println("  fisier unic:   java HtmlPacker.java <input.html> <output.html>");
            System.err.println("  folder (1000): java HtmlPacker.java <inputDir> <outputDir>");
            System.exit(1);
        }
        Path input = Path.of(args[0]);
        Path output = Path.of(args[1]);

        if (Files.isDirectory(input)) {
            Files.createDirectories(output);
            // decoder.js se scrie o SINGURA data, e comun tuturor paginilor
            Files.writeString(output.resolve("decoder.js"), DECODER_JS, StandardCharsets.UTF_8);

            long totalOriginal = 0, totalNew = 0;
            int count = 0;
            try (var files = Files.walk(input)) {
                for (Path p : (Iterable<Path>) files.filter(f -> f.toString().endsWith(".html"))::iterator) {
                    Path rel = input.relativize(p);
                    Path out = output.resolve(rel);
                    Files.createDirectories(out.getParent() == null ? output : out.getParent());
                    long[] sizes = packOne(p, out);
                    totalOriginal += sizes[0];
                    totalNew += sizes[1];
                    count++;
                }
            }
            System.out.printf("Procesate %d fisiere. Total: %,d B -> %,d B (reducere %.1f%%)%n",
                    count, totalOriginal, totalNew,
                    100.0 * (totalOriginal - totalNew) / totalOriginal);
        } else {
            long[] sizes = packOne(input, output);
            System.out.printf(
                    "Original: %,d B  |  Shell final: %,d B  |  Reducere: %.1f%%%n",
                    sizes[0], sizes[1], 100.0 * (sizes[0] - sizes[1]) / sizes[0]);
        }
    }

    /** Comprima un fisier si scrie shell-ul; returneaza {originalSize, newSize}. */
    private static long[] packOne(Path input, Path output) throws IOException {
        byte[] html = Files.readAllBytes(input);
        byte[] compressed = deflateRaw(html);
        String b64 = Base64.getEncoder().encodeToString(compressed);

        String shell = """
                <!DOCTYPE html><html><head><meta charset="utf-8"></head><body>
                <script src="decoder.js"></script>
                <script id="p" type="application/octet-stream+b64">%s</script>
                </body></html>
                """.formatted(b64);

        Files.writeString(output, shell, StandardCharsets.UTF_8);
        return new long[] { Files.size(input), Files.size(output) };
    }

    private static final String DECODER_JS = """
            (async function () {
                const tag = document.getElementById('p');
                const b64 = tag.textContent;
                const binary = Uint8Array.from(atob(b64), c => c.charCodeAt(0));
                const ds = new DecompressionStream('deflate-raw');
                const stream = new Blob([binary]).stream().pipeThrough(ds);
                const html = await new Response(stream).text();
                document.open();
                document.write(html);
                document.close();
            })();
            """;

    private static byte[] deflateRaw(byte[] data) throws IOException {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, /*nowrap=*/ true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater)) {
            dos.write(data);
        }
        deflater.end();
        return baos.toByteArray();
    }
}

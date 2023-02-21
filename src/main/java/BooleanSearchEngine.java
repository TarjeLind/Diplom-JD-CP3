import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private final Map<String, List<PageEntry>> index = new HashMap<>();


    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] listPdfFiles = getListFiles(pdfsDir, ".pdf");
        if (listPdfFiles != null) {
            for (File pdfFile : listPdfFiles) {
                scan(pdfFile);
            }
            index.values().forEach(Collections::sort);
        }
    }


    private void scan(File pdfFile) throws IOException {

        try (var doc = new PdfDocument(new PdfReader(pdfFile))) {

            int numberOfPages = doc.getNumberOfPages();

            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = doc.getPage(i);

                var text = PdfTextExtractor.getTextFromPage(page);

                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);

                }
                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    List<PageEntry> listOFCurrentWord = new ArrayList<>();
                    if (index.containsKey(entry.getKey())) {
                        listOFCurrentWord = index.get(entry.getKey());
                    }
                    listOFCurrentWord.add(new PageEntry(pdfFile.getName(), i, entry.getValue()));
                    index.put(entry.getKey(), listOFCurrentWord);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (index.containsKey(word.toLowerCase())) {
            return index.get(word.toLowerCase());
        }
        return Collections.emptyList();
    }

    private File[] getListFiles(File pdfsDir, String s) {
        return pdfsDir.listFiles();
    }

}
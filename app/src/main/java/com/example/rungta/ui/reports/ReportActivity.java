package com.example.rungta.ui.reports;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rungta.data.model.AttendanceRecord;
import com.example.rungta.databinding.ActivityReportBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private ActivityReportBinding binding;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        firestore = FirebaseFirestore.getInstance();

        binding.btnGeneratePdf.setOnClickListener(v -> generatePdfReport());
    }

    private void generatePdfReport() {
        firestore.collection("attendance").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<AttendanceRecord> records = new ArrayList<>();
            Map<String, int[]> subjectStats = new HashMap<>(); // subject -> [present, total]

            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                records.add(record);

                // Subject stats
                String subject = record.getSubject();
                if (subject != null && !subject.isEmpty()) {
                    int[] sStat = subjectStats.get(subject);
                    if (sStat == null) sStat = new int[]{0, 0};
                    if (record.isPresent()) sStat[0]++;
                    sStat[1]++;
                    subjectStats.put(subject, sStat);
                }
            }

            if (records.isEmpty()) {
                Toast.makeText(this, "No attendance records found to generate report", Toast.LENGTH_SHORT).show();
                return;
            }

            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();
            Paint titlePaint = new Paint();
            Paint headerPaint = new Paint();

            int pageWidth = 595; // A4 width
            int pageHeight = 842; // A4 height
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // College Header & Branding
            titlePaint.setAntiAlias(true);
            titlePaint.setTextSize(20f);
            titlePaint.setFakeBoldText(true);
            titlePaint.setColor(Color.rgb(33, 150, 243));
            canvas.drawText("RUNGTA COLLEGE OF ENGINEERING", 40, 50, titlePaint);

            titlePaint.setTextSize(14f);
            titlePaint.setColor(Color.DKGRAY);
            canvas.drawText("Comprehensive Attendance Analytics Report", 40, 75, titlePaint);

            paint.setTextSize(10f);
            paint.setColor(Color.GRAY);
            String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
            canvas.drawText("Generated On: " + currentDate, 40, 95, paint);

            canvas.drawLine(40, 110, 555, 110, paint);

            int y = 135;

            // Subject-wise Summary Section
            headerPaint.setTextSize(14f);
            headerPaint.setFakeBoldText(true);
            headerPaint.setColor(Color.BLACK);
            canvas.drawText("Subject-Wise Summary Report", 40, y, headerPaint);
            y += 20;

            paint.setTextSize(11f);
            paint.setFakeBoldText(true);
            canvas.drawText("Subject Name", 40, y, paint);
            canvas.drawText("Present / Total", 300, y, paint);
            canvas.drawText("Attendance %", 450, y, paint);
            y += 8;
            canvas.drawLine(40, y, 555, y, paint);
            y += 18;

            paint.setFakeBoldText(false);
            for (Map.Entry<String, int[]> entry : subjectStats.entrySet()) {
                if (y > pageHeight - 50) {
                    pdfDocument.finishPage(page);
                    pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.getPages().size() + 1).create();
                    page = pdfDocument.startPage(pageInfo);
                    canvas = page.getCanvas();
                    y = 50;
                }
                String subj = entry.getKey();
                int present = entry.getValue()[0];
                int total = entry.getValue()[1];
                float pct = total > 0 ? ((float) present / total) * 100f : 0f;

                canvas.drawText(subj, 40, y, paint);
                canvas.drawText(present + " / " + total, 300, y, paint);
                canvas.drawText(String.format(Locale.getDefault(), "%.1f%%", pct), 450, y, paint);
                y += 22;
            }

            y += 20;
            if (y > pageHeight - 50) {
                pdfDocument.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.getPages().size() + 1).create();
                page = pdfDocument.startPage(pageInfo);
                canvas = page.getCanvas();
                y = 50;
            }

            // Detailed Attendance Records Section
            canvas.drawText("Detailed Attendance Records", 40, y, headerPaint);
            y += 20;

            paint.setFakeBoldText(true);
            canvas.drawText("Student Name", 40, y, paint);
            canvas.drawText("Roll No", 180, y, paint);
            canvas.drawText("Subject", 280, y, paint);
            canvas.drawText("Date", 400, y, paint);
            canvas.drawText("Status", 480, y, paint);
            y += 8;
            canvas.drawLine(40, y, 555, y, paint);
            y += 18;

            paint.setTextSize(10f);
            paint.setFakeBoldText(false);
            for (AttendanceRecord record : records) {
                if (y > pageHeight - 50) {
                    pdfDocument.finishPage(page);
                    pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdfDocument.getPages().size() + 1).create();
                    page = pdfDocument.startPage(pageInfo);
                    canvas = page.getCanvas();
                    y = 50;
                }

                canvas.drawText(record.getStudentName() != null ? record.getStudentName() : "", 40, y, paint);
                canvas.drawText(record.getRollNo() != null ? record.getRollNo() : "", 180, y, paint);
                canvas.drawText(record.getSubject() != null ? record.getSubject() : "", 280, y, paint);
                canvas.drawText(record.getDate() != null ? record.getDate() : "", 400, y, paint);
                canvas.drawText(record.isPresent() ? "Present" : "Absent", 480, y, paint);
                y += 22;
            }

            pdfDocument.finishPage(page);

            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, "College_Attendance_Report_" + System.currentTimeMillis() + ".pdf");

            try {
                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(this, "PDF Report saved to Downloads: " + file.getName(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                pdfDocument.close();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to generate report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}

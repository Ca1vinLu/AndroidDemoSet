package com.lvgodness.printerhelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.print.PrintAttributes.COLOR_MODE_MONOCHROME;
import static android.support.v4.print.PrintHelper.SCALE_MODE_FILL;

/**
 * Created by LYZ on 2017/11/8 0008.
 */

public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    private static final String TAG = "MyPrintDocumentAdapter";
    protected boolean mIsMinMarginsHandlingCorrect = true;
    private PrintedPdfDocument mPdfDocument;
    private Context context;
    private List<Bitmap> data;

    private PrintAttributes attributes;
    private RectF contentRect;
    private Matrix matrix;


    public MyPrintDocumentAdapter(Context context, @NonNull List<Bitmap> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        // Create a new PdfDocument with the requested page attributes
        mPdfDocument = new PrintedPdfDocument(context, newAttributes);
        attributes = newAttributes;
        // Respond to cancellation request
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        // Compute the expected number of printed pages
//        int pages = computePageCount(newAttributes);
        int pages = data.size();

        if (pages > 0) {
            // Return print information to print framework
            PrintDocumentInfo info = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_PHOTO)
                    .setPageCount(pages)
                    .build();
            // Content layout reflow is complete
            callback.onLayoutFinished(info, true);
        } else {
            // Otherwise report an error to the print framework
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 1; // default item count for portrait mode

        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
        if (!pageSize.isPortrait()) {
            // Six items per page in landscape orientation
            itemsPerPage = 1;
        }

        // Determine number of print items
        int printItemCount = data.size();

        return (int) Math.ceil(printItemCount / itemsPerPage);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, final CancellationSignal cancellationSignal, WriteResultCallback callback) {
//        final PrintAttributes pdfAttributes;
//        if (mIsMinMarginsHandlingCorrect) {
//            pdfAttributes = attributes;
//        } else {
//            // If the handling of any margin != 0 is broken, strip the margins and add them to the
//            // bitmap later
//            pdfAttributes = copyAttributes(attributes)
//                    .setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
//        }
//
//        (new AsyncTask<Void, Void, Throwable>() {
//            @Override
//            protected Throwable doInBackground(Void... params) {
//                try {
//                    if (cancellationSignal.isCanceled()) {
//                        return null;
//                    }
//
//                    PrintedPdfDocument pdfDocument = new PrintedPdfDocument(context,
//                            pdfAttributes);
//
//                    Bitmap maybeGrayscale = convertBitmapForColorMode(bitmap,
//                            pdfAttributes.getColorMode());
//
//                    if (cancellationSignal.isCanceled()) {
//                        return null;
//                    }
//
//                    try {
//                        PdfDocument.Page page = pdfDocument.startPage(1);
//
//                        RectF contentRect;
//                        if (mIsMinMarginsHandlingCorrect) {
//                            contentRect = new RectF(page.getInfo().getContentRect());
//                        } else {
//                            // Create dummy doc that has the margins to compute correctly sized
//                            // content rectangle
//                            PrintedPdfDocument dummyDocument = new PrintedPdfDocument(context,
//                                    attributes);
//                            PdfDocument.Page dummyPage = dummyDocument.startPage(1);
//                            contentRect = new RectF(dummyPage.getInfo().getContentRect());
//                            dummyDocument.finishPage(dummyPage);
//                            dummyDocument.close();
//                        }
//
//                        // Resize bitmap
//                        Matrix matrix = getMatrix(
//                                maybeGrayscale.getWidth(), maybeGrayscale.getHeight(),
//                                contentRect, fittingMode);
//
//                        if (mIsMinMarginsHandlingCorrect) {
//                            // The pdfDocument takes care of the positioning and margins
//                        } else {
//                            // Move it to the correct position.
//                            matrix.postTranslate(contentRect.left, contentRect.top);
//
//                            // Cut off margins
//                            page.getCanvas().clipRect(contentRect);
//                        }
//
//                        // Draw the bitmap.
//                        page.getCanvas().drawBitmap(maybeGrayscale, matrix, null);
//
//                        // Finish the page.
//                        pdfDocument.finishPage(page);
//
//                        if (cancellationSignal.isCanceled()) {
//                            return null;
//                        }
//
//                        // Write the document.
//                        pdfDocument
//                                .writeTo(new FileOutputStream(fileDescriptor.getFileDescriptor()));
//                        return null;
//                    } finally {
//                        pdfDocument.close();
//
//                        if (fileDescriptor != null) {
//                            try {
//                                fileDescriptor.close();
//                            } catch (IOException ioe) {
//                                // ignore
//                            }
//                        }
//                        // If we created a new instance for grayscaling, then recycle it here.
//                        if (maybeGrayscale != bitmap) {
//                            maybeGrayscale.recycle();
//                        }
//                    }
//                } catch (Throwable t) {
//                    return t;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Throwable throwable) {
//                if (cancellationSignal.isCanceled()) {
//                    // Cancelled.
//                    writeResultCallback.onWriteCancelled();
//                } else if (throwable == null) {
//                    // Done.
//                    writeResultCallback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
//                } else {
//                    // Failed.
//                    Log.e(TAG, "Error writing printed content", throwable);
//                    writeResultCallback.onWriteFailed(null);
//                }
//            }
//        }).execute();

//        List<PageRange> pageRanges = new ArrayList<>();
        // Iterate over each page of the document,
        // check if it's in the output range.
        for (int i = 0; i < data.size(); i++) {
            // Check to see if this page is in the output range.

            // If so, add it to writtenPagesArray. writtenPagesArray.size()
            // is used to compute the next output page index.
//            pageRanges.add();
            PdfDocument.Page page = mPdfDocument.startPage(i);

            // check for cancellation
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
                mPdfDocument.close();
                mPdfDocument = null;
                return;
            }

            // Draw page content for printing
            drawPage(page, data.get(i));

            // Rendering is complete, so page can be finalized.
            mPdfDocument.finishPage(page);

        }

        // Write PDF document to file
        try {
            mPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            mPdfDocument.close();
            mPdfDocument = null;
        }
//        PageRange[] writtenPages = computeWrittenPages();
        // Signal the print framework the document is complete
        callback.onWriteFinished(pages);

    }

    private Bitmap convertBitmapForColorMode(Bitmap original, int colorMode) {
        if (colorMode != COLOR_MODE_MONOCHROME) {
            return original;
        }
        // Create a grayscale bitmap
        Bitmap grayscale = Bitmap.createBitmap(original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(grayscale);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        p.setColorFilter(f);
        c.drawBitmap(original, 0, 0, p);
        c.setBitmap(null);

        return grayscale;
    }

    protected PrintAttributes.Builder copyAttributes(@NonNull PrintAttributes other) {
        PrintAttributes.Builder b = (new PrintAttributes.Builder())
                .setMediaSize(other.getMediaSize())
                .setResolution(other.getResolution())
                .setMinMargins(other.getMinMargins());

        if (other.getColorMode() != 0) {
            b.setColorMode(other.getColorMode());
        }

        return b;
    }

    private void drawPage(PdfDocument.Page page, Bitmap bitmap) {
        Canvas canvas = page.getCanvas();
        if (contentRect == null)
            contentRect = new RectF(page.getInfo().getContentRect());

        // units are in points (1/72 of an inch)
        if (matrix == null)
            matrix = getMatrix(
                    bitmap.getWidth(), bitmap.getHeight(),
                    contentRect, SCALE_MODE_FILL);


        canvas.drawBitmap(bitmap, matrix, null);
    }

    private Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
        Matrix matrix = new Matrix();

        // Compute and apply scale to fill the page.
        float scale = content.width() / imageWidth;
        if (fittingMode == SCALE_MODE_FILL) {
            scale = Math.max(scale, (content.width() - 50) / imageWidth);
        } else {
            scale = Math.min(scale, (content.width() - 50) / imageWidth);
        }
        matrix.postScale(scale, scale);

        // Center the content.
        final float translateX = (content.width()
                - imageWidth * scale) / 2;
        final float translateY = (content.height()
                - imageHeight * scale) / 2;
        matrix.postTranslate(translateX, translateY);
        return matrix;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}

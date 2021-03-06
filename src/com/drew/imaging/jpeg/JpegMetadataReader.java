/*
 * This is public domain software - that is, you can do whatever you want
 * with it, and include it software that is licensed under the GNU or the
 * BSD license, or whatever other licence you choose, including proprietary
 * closed source licenses.  I do ask that you leave this header in tact.
 *
 * If you make modifications to this code that you think would benefit the
 * wider community, please send me a copy and I'll post it on my site.
 *
 * If you make use of this code, I'd appreciate hearing about it.
 *   drew@drewnoakes.com
 * Latest version of this software kept at
 *   http://drewnoakes.com/
 *
 * Created by dnoakes on 12-Nov-2002 18:51:36 using IntelliJ IDEA.
 */
package com.drew.imaging.jpeg;

//import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
//import com.drew.metadata.MetadataException;
//import com.drew.metadata.Tag;
//import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.exif.ExifReader;
//import com.drew.metadata.iptc.IptcReader;
import com.drew.metadata.jpeg.JpegCommentReader;
import com.drew.metadata.jpeg.JpegReader;

import java.io.File;
//import java.io.IOException;
import java.io.InputStream;
//import java.util.Iterator;

/**
 *
 */
public class JpegMetadataReader
{
//    public static Metadata readMetadata(IIOMetadata metadata) throws JpegProcessingException {}
//    public static Metadata readMetadata(ImageInputStream in) throws JpegProcessingException{}
//    public static Metadata readMetadata(IIOImage image) throws JpegProcessingException{}
//    public static Metadata readMetadata(ImageReader reader) throws JpegProcessingException{}

    public static Metadata readMetadata(InputStream in) throws JpegProcessingException
    {
        JpegSegmentReader segmentReader = new JpegSegmentReader(in);
        return extractMetadataFromJpegSegmentReader(segmentReader);
    }

    public static Metadata readMetadata(File file) throws JpegProcessingException
    {
        JpegSegmentReader segmentReader = new JpegSegmentReader(file);
        return extractMetadataFromJpegSegmentReader(segmentReader);
    }

    public static Metadata extractMetadataFromJpegSegmentReader(JpegSegmentReader segmentReader)
    {
        final Metadata metadata = new Metadata();
        try {
            byte[] exifSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_APP1);
            new ExifReader(exifSegment).extract(metadata);
        } catch (JpegProcessingException e) {
            // in the interests of catching as much data as possible, continue
            // TODO lodge error message within exif directory?
        }

//        try {
//            byte[] iptcSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_APPD);
//            new IptcReader(iptcSegment).extract(metadata);
//        } catch (JpegProcessingException e) {
//            // TODO lodge error message within iptc directory?
//        }

		try {
			byte[] jpegSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_SOF0);
			new JpegReader(jpegSegment).extract(metadata);
		} catch (JpegProcessingException e) {
			// TODO lodge error message within jpeg directory?
		}

		try {
			byte[] jpegCommentSegment = segmentReader.readSegment(JpegSegmentReader.SEGMENT_COM);
			new JpegCommentReader(jpegCommentSegment).extract(metadata);
		} catch (JpegProcessingException e) {
			// TODO lodge error message within jpegcomment directory?
		}

        return metadata;
    }

    private JpegMetadataReader()
    {
    }

//    public static void main(String[] args) throws MetadataException, IOException
//    {
//        Metadata metadata = null;
//        try {
//            metadata = JpegMetadataReader.readMetadata(new File(args[0]));
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//            System.exit(1);
//        }
//
//        // iterate over the exif data and print to System.out
//        Iterator directories = metadata.getDirectoryIterator();
//        while (directories.hasNext()) {
//            Directory directory = (Directory)directories.next();
//            Iterator tags = directory.getTagIterator();
//            while (tags.hasNext()) {
//                Tag tag = (Tag)tags.next();
//                try {
//                    System.out.println("[" + directory.getName() + "] " + tag.getTagName() + " = " + tag.getDescription());
//                } catch (MetadataException e) {
//                    System.err.println(e.getMessage());
//                    System.err.println(tag.getDirectoryName() + " " + tag.getTagName() + " (error)");
//                }
//            }
//            if (directory.hasErrors()) {
//                Iterator errors = directory.getErrors();
//                while (errors.hasNext()) {
//                    System.out.println("ERROR: " + errors.next());
//                }
//            }
//        }
//
//        if (args.length>1 && args[1].trim().equals("/thumb"))
//        {
//            ExifDirectory directory = (ExifDirectory)metadata.getDirectory(ExifDirectory.class);
//            if (directory.containsThumbnail())
//            {
//                System.out.println("Writing thumbnail...");
//                directory.writeThumbnail(args[0].trim() + ".thumb.jpg");
//            }
//            else
//            {
//                System.out.println("No thumbnail data exists in this image");
//            }
//        }
//    }
}

package uz.innavation

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaExtractor
import android.media.MediaFormat
import java.nio.ByteBuffer

class Just {
    val extractor = MediaExtractor()

    fun encode(){
        val extractor = MediaExtractor()
        extractor.setDataSource("inFilePath")

        for (i in 0 until extractor.trackCount) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME);

            if (mime!!.startsWith("video/")) {

                extractor.selectTrack(i)
                // Read the frames from this track here
                // ...
            }
        }
    }

    fun two(){
        val maxChunkSize = 1024 * 1024
        val buffer = ByteBuffer.allocate(maxChunkSize)
        val bufferInfo = MediaCodec.BufferInfo()

// Extract all frames from selected track
        while (true) {
            val chunkSize = extractor.readSampleData(buffer, 0)

            if (chunkSize > 0) {
                // Process extracted frame here
                // ...

                extractor.advance()

            } else {
                // All frames extracted - we're done
                break
            }
        }
    }

    fun three(){
        val mime = "video/avc"
        val width = 320; val height = 180
        val outFormat = MediaFormat.createVideoFormat(mime, width, height)
        outFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        outFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2000000)
        outFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
        outFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 15)
        outFormat.setString(MediaFormat.KEY_MIME, mime)

// Init ecoder
//        encoder = MediaCodec.createEncoderByType(outFormat.getString(MediaFormat.KEY_MIME))
//        encoder.configure(outFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
//        inputSurface = encoder.createInputSurface()
    }
}
package com.google.sps;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import com.google.sps.data.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.sps.servlets.*;
import java.util.*;
import java.lang.Long;

@RunWith(JUnit4.class)
public final class CollectVideosTest {
    private static final String videoUrl1 = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    private static final String videoUrl2 = "https://www.youtube.com/watch?v=nhPcn-2iHJ4";
    private static final String videoUrl3 = "https://www.youtube.com/watch?v=gqDRFD5qOx8";

    @Test
    public void TestExtractVideoUrls(){
        // Create test Video objects
        Video testVideo1 = Video.createVideo(videoUrl1);
        Video testVideo2 = Video.createVideo(videoUrl2);
        Video testVideo3 = Video.createVideo(videoUrl3);
        //Add videos to queue for testing
        Queue<Video> testVideoQueue = new LinkedList<Video>();
        testVideoQueue.add(testVideo1);
        testVideoQueue.add(testVideo2);
        testVideoQueue.add(testVideo3);
        // Add video urls to expected list of urls
        List<String> expectedList = new ArrayList<>();
        expectedList.add(videoUrl1);
        expectedList.add(videoUrl2);
        expectedList.add(videoUrl3);

        List<String> actualList = CollectVideosServlet.extractVideoUrls(testVideoQueue);

        Assert.assertEquals(expectedList, actualList);
    }
}
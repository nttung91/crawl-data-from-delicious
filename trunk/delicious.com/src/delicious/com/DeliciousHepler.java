/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delicious.com;

import java.sql.Timestamp;
import java.util.ArrayList;
import lib.tools.HtmlContent;
import lib.tools.MD5Convertor;
import model.dao.DocumentDAO;
import model.dao.PostDAO;
import model.dao.TagDAO;
import model.dao.TagsForPostDAO;
import model.pojo.Document;
import model.pojo.Posts;
import model.pojo.Tag;
import model.pojo.TagsForPost;
import model.pojo.TagsForPostId;
import org.hibernate.HibernateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author THANHTUNG
 */
public class DeliciousHepler {

    private static String getResponeData(String url) {
        HtmlContent hc = new HtmlContent();
        String res = hc.getHtmlContent(url);
        return res;
    }
    
    public static ArrayList<String> getRecentListBookmarkByTag(String tag) throws ParseException
    {
        JSONParser jsonParser = new JSONParser();
        String bookmarks = getResponeData(String.format("http://feeds.delicious.com/v2/json/tag/%s?count=100",tag));
         if (bookmarks != null) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(bookmarks);
            ArrayList<String> l =new ArrayList<>(); 
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                if (obj.get("u") != null && !obj.get("u").toString().equals("")) {
                    l.add(obj.get("u").toString().trim());
                }
                
            }
            return l;
         }
         return null;
    }
    public static void getAndSaveBookmarkHistory(String bookmark) throws ParseException {
        JSONParser jsonParser = new JSONParser();

        String jsonDataString = "";
        bookmark = MD5Convertor.Convert2MD5(bookmark);
        //get number of post in this link
        String linkInfo = getResponeData(String.format("http://feeds.delicious.com/v2/json/urlinfo/%s", bookmark));
        


        int totalPost = 0;
        int DocID = DocumentDAO.nextIndex();
        //System.out.println(totalPost);

        jsonDataString = getResponeData(String.format("http://feeds.delicious.com/v2/json/url/%s?count=%d", bookmark, totalPost));
        if (jsonDataString != null) {
            try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonDataString);
          

            for (int i = 0; i < jsonArray.size(); i++) {
                Posts post = new Posts();
                post.setPostId(PostDAO.nextIndex());
                JSONObject obj = (JSONObject) jsonArray.get(i);

                if (obj.get("a") != null) {
                    post.setAuthor(obj.get("a").toString());
                    if (obj.get("a").toString().equals("")) continue;
                }
                
                if (obj.get("d") != null) {
                    post.setDescription(obj.get("d").toString());
                }
                if (obj.get("n") != null) {
                    post.setNote(obj.get("n").toString());
                }
                if (obj.get("dt") != null) {
                    String date = obj.get("dt").toString();
                    date = date.replace("T", " ").replace("Z","");
                   // System.out.println(date);
                    post.setDatePost(Timestamp.valueOf(date));
                }
                Document doc = new Document();
                if (linkInfo != null) {

                    JSONArray infoArray = (JSONArray) jsonParser.parse(linkInfo);
                    totalPost = Integer.parseInt(((JSONObject) infoArray.get(0)).get("total_posts").toString());

                    doc.setDocumentId(DocID);
                    if (((JSONObject) infoArray.get(0)).get("hash") != null) {
                        doc.setHash(((JSONObject) infoArray.get(0)).get("hash").toString());
                    }
                    if (((JSONObject) infoArray.get(0)).get("title") != null) {
                        doc.setTitle(((JSONObject) infoArray.get(0)).get("title").toString());
                    }

                    doc.setTotalPosts(totalPost);
                    if (((JSONObject) infoArray.get(0)).get("url") != null) {
                        doc.setUrl(((JSONObject) infoArray.get(0)).get("url").toString());
                    }
                }
                post.setDocument(doc);
                try {
                    PostDAO.saveOrUpdateObject(post);
                } catch (HibernateException ex) {
                    ex.printStackTrace();
                }
                //Set<Tag> tags = new HashSet<>();
                if (obj.get("t") != null) {

                    JSONArray arrTag = (JSONArray) obj.get("t");
                    for (int j = 0; j < arrTag.size(); j++) {
                        String objtag = (String) arrTag.get(j);
                        Tag tag = new Tag(TagDAO.nextIndex(objtag));
                         //tag.setTagId(maxTag);
                        tag.setTagName(objtag);
                        TagsForPostId id = new TagsForPostId(tag.getTagId(), post.getPostId());
                        TagsForPost tfp = new TagsForPost(id, tag, post);
                        TagsForPostDAO.saveOrUpdateObject(tfp);
                    }

                }

                //PostDAO.saveOrUpdateObject(post);
            }
            }catch (Exception ex)
            {
                
            }
           
        }


    }
}

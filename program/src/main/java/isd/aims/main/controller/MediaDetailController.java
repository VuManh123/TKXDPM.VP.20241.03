package isd.aims.main.controller;

import isd.aims.main.entity.media.Media;

import java.sql.SQLException;
import java.util.List;


/**
 * This class controls the flow of events in homescreen
 * @author nguyenlm
 */
public class MediaDetailController extends BaseController{


    /**
     * this method gets all Media in DB and return back to home to display
     * @return List[Media]
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
    public List getAllMedia() throws SQLException{
        return new Media().getAllMedia();
    }
}

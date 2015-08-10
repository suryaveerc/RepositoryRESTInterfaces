/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presence.services.rest;

import com.presence.dao.WatcherDAO;
import com.presence.model.Watchers;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Suryaveer
 */
@Path("/V1/Watchers")
public class WatcherService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WatcherService.class);

    public Response addWatcher(Watchers watcher) {
        int status = 0;
        WatcherDAO watcherDAO = new WatcherDAO();
        try {
            status = watcherDAO.addWatcher(watcher);
            logger.debug("Record for watcher-presentity ({} - {}) processed with status: {} ", watcher.getWatcherUsername(),watcher.getPresentityUri(),status);
            return Response.status(201).build();
        } catch (Exception e) {
            logger.debug("Error while processing watcher-presentity ({} - {}) ", watcher.getWatcherUsername(),watcher.getPresentityUri(),e);
            return Response.status(500).entity("Server was unable to process the request.").build();
        }

    }
}

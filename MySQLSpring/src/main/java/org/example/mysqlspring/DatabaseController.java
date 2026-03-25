package org.example.mysqlspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/databases")
    public List<String> getDatabase() {
        return databaseService.listDatabases();
    }

    @GetMapping("/databases/{dbName}/tables")
    public List<String> getTables(@PathVariable String dbName){
        return databaseService.listTables(dbName);
    }

}

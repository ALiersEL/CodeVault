package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.controller.entity.FileWithTypes;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FolderMapper {
    @Select("SELECT f1.folder_id AS id, f1.folder_name AS name, '文件夹' AS type, f1.last_modified " +
            "FROM folder AS f1 "  +
            "INNER JOIN folder AS f2 " +
            "ON f1.parent_folder_id = f2.folder_id " +
            "WHERE f1.user_id = #{userID} AND f2.folder_path = #{folderPath} " +
            "UNION " +
            "SELECT problem_id AS id, problem_title AS name, '文件' AS type, problem.last_modified " +
            "FROM problem " +
            "INNER JOIN folder " +
            "ON problem.folder_id = folder.folder_id " +
            "WHERE problem.user_id = #{userID} AND folder_path = #{folderPath}")
    List<FileWithTypes> findFilesByFolderPath(Integer userID, String folderPath);

    @Insert("INSERT INTO folder (folder_name, folder_path, parent_folder_id, user_id) " +
            "VALUES (#{folderName}, #{folderPath}, (SELECT folder_id FROM folder WHERE folder_path = #{parentPath}), #{userID})")
    Boolean saveFolder(Integer userID, String folderPath, String parentPath, String folderName);


    // folderID为主键，不同用户不可能有相同的folderID
    @Delete("BEGIN; " +
            "WITH RECURSIVE subfolders AS ( " +
            "SELECT folder_id " +
            "FROM folder " +
            "WHERE folder_id = #{folderID} " +
            "UNION ALL " +
            "SELECT f.folder_id " +
            "FROM folder AS f " +
            "INNER JOIN subfolders AS sf " +
            "ON f.parent_folder_id = sf.folder_id " +
            ") " +
            "DELETE FROM problem " +
            "WHERE folder_id IN (SELECT folder_id FROM subfolders); " +
            "DELETE FROM folder WHERE folder_id = #{folderID}; " +
            "COMMIT; ")
    Boolean deleteFolder(Integer userID, Integer folderID);
}

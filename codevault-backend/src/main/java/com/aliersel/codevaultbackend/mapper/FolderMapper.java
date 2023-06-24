package com.aliersel.codevaultbackend.mapper;

import com.aliersel.codevaultbackend.controller.api.FileWithTypes;
import com.aliersel.codevaultbackend.entity.Folder;
import org.apache.ibatis.annotations.*;

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

    @Select("SELECT f1.folder_id AS id, f1.folder_name AS name, '文件夹' AS type, f1.last_modified " +
            "FROM folder AS f1 "  +
            "INNER JOIN folder AS f2 " +
            "ON f1.parent_folder_id = f2.folder_id " +
            "WHERE f1.user_id = #{userID} AND f2.folder_id = #{folderID} " +
            "UNION " +
            "SELECT problem_id AS id, problem_title AS name, '文件' AS type, problem.last_modified " +
            "FROM problem " +
            "INNER JOIN folder " +
            "ON problem.folder_id = folder.folder_id " +
            "WHERE problem.user_id = #{userID} AND problem.folder_id = #{folderID}")
    List<FileWithTypes> findFilesByFolderID(Integer userID, Integer folderID);

    @Select("SELECT * FROM folder WHERE parent_folder_id = #{folderID}")
    List<Folder> findFoldersByFolderID(Integer folderID);

    @Insert("INSERT INTO folder (folder_name, folder_path, parent_folder_id, user_id) " +
            "VALUES (#{folderName}, #{folderPath}, (SELECT folder_id FROM folder WHERE user_id = #{userID} AND folder_path = #{parentPath}), #{userID})")
    Boolean saveFolder(Integer userID, String folderPath, String parentPath, String folderName);


    // folderID为主键，不同用户不可能有相同的folderID
    @Delete("DELETE FROM folder WHERE folder_id = #{folderID}")
    Boolean deleteFolder(Integer userID, Integer folderID);

    @Select("SELECT folder_id FROM folder WHERE user_id = #{userID} AND folder_path = #{folderPath}")
    Integer findFolderIDByFolderPath(Integer userID, String folderPath);

    // 更新该folderID的folderName, 并将该folderID所有子文件夹的folderPath更新
    @Update("UPDATE folder " +
            "SET folder_name = #{newName}, folder_path = #{newPath} " +
            "WHERE folder_id = #{folderID}; -- 更新指定文件夹的名称\n" +

            "WITH RECURSIVE subfolders AS ( " +
            "    -- 获取指定文件夹及其所有子文件夹的信息\n" +
            "    SELECT folder_id, folder_name, folder_path " +
            "    FROM folder " +
            "    WHERE folder_id = #{folderID} -- 指定要更改的文件夹的ID\n" +

            "    UNION ALL\n" +

            "    -- 递归获取子文件夹的信息\n" +
            "    SELECT f.folder_id, f.folder_name, sf.folder_path || '/' || f.folder_name " +
            "    FROM folder f " +
            "    INNER JOIN subfolders AS sf ON f.parent_folder_id = sf.folder_id " +
            ")" +

            "UPDATE folder AS f " +
            "SET folder_path = sf.folder_path " +
            "FROM subfolders AS sf " +
            "WHERE f.folder_id = sf.folder_id; ")
    Boolean updateFolderName(Integer folderID, String newName, String newPath);

    @Update("UPDATE folder SET parent_folder_id = #{targetFolderID} WHERE folder_id = #{folderID}")
    Boolean updateFolderParent(Integer folderID, Integer targetFolderID);
}

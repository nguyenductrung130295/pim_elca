package vn.elca.training.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import vn.elca.training.entities.Group;
import vn.elca.training.services.IGroupDaoService;

@Service
public class GroupDaoImp implements IGroupDaoService {
    private static List<Group> list = new ArrayList<>();

    public GroupDaoImp() {
        list.add(new Group(Long.valueOf(1), 1, "CIM"));
        list.add(new Group(Long.valueOf(2), 1, "JAVA"));
        list.add(new Group(Long.valueOf(3), 1, ".NET"));
        list.add(new Group(Long.valueOf(4), 1, "Secutix"));
    }

    @Override
    public List<Group> getAll() {
        // TODO Auto-generated method stub
        return list;
    }
}

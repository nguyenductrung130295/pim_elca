package vn.elca.training.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.elca.training.dao.IGroupRepository;
import vn.elca.training.entities.Group;

@Service
public class GroupServiceImp implements IGroupService {
    @Autowired
    IGroupRepository groupRepository;

    @Override
    public List<Group> getAllGroup() {
        return groupRepository.findAll();
    }
}

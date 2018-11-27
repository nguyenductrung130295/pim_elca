package vn.elca.training.services;

import java.util.List;

import vn.elca.training.entities.Group;

public interface IGroupDaoService {
    List<Group> getAll();
}

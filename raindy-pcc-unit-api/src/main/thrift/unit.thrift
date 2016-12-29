namespace  java com.archnotes.raindy.pcc.unit.api

service UnitService {

    bool addUser(1:string name);
    string getUser(1:string userId);
    bool followUser(1:string sourceUid, 2:string followUid);

    bool like (1:string targetId, 2:string ownId, 3:string userId);
    set<string> getLikes (1:string targetId);
    i64 getLikesCount (1:string targetId);
    bool isLike (1:string targetId, 2:string userId);
}
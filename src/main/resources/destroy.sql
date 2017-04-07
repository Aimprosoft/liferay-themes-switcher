DELETE FROM ts_look_and_feel_binding;
DELETE FROM ts_look_and_feel;
DROP TABLE ts_look_and_feel_binding;
DROP TABLE ts_look_and_feel;
DELETE FROM resourcepermission
WHERE
  name = 'com.aimprosoft.lfs.model.persist.LookAndFeel'
  OR
  name = 'themesswitcher_WAR_themesswitcherportlet';
DELETE FROM resourceaction
WHERE
  name = 'com.aimprosoft.lfs.model.persist.LookAndFeel'
  OR
  name = 'themesswitcher_WAR_themesswitcherportlet';
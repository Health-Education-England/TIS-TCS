-- fix trainingPathway for 401 records with null ProgrammeMembershipType

-- trainingPathway should be set to CCT, 174 in total
UPDATE `ProgrammeMembership` SET `trainingPathway` = 'CCT' where `id` in (122283, 129533, 146738, 144650, 136199, 131921, 152906, 191674, 229095, 130700, 127958, 142549, 115605, 139859, 144147, 130049, 135561, 149211, 149868, 115188, 228685, 236073, 125897, 144830, 124545, 144527, 149927, 138367, 138132, 134008, 149403, 140519, 230070, 135344, 128457, 148847, 148726, 137191, 146488, 130422, 147455, 136211, 140171, 144036, 145292, 131987, 132220, 149335, 143926, 135652, 135912, 127062, 143740, 147792, 126864, 151934, 127879, 147298, 149283, 131496, 140574, 150983, 143363, 134301, 148230, 149393, 139088, 135956, 148427, 151761, 147412, 131695, 140551, 138440, 139995, 144164, 128971, 144441, 136745, 127208, 140505, 149629, 144271, 144933, 145736, 144910, 151075, 143301, 143574, 135343, 133585, 128592, 129860, 142603, 142036, 126790, 130472, 143896, 142810, 138463, 135133, 151422, 148021, 151533, 134210, 143955, 136789, 128313, 136454, 141505, 130568, 137441, 135386, 145010, 131931, 128174, 151241, 142460, 128844, 148603, 143791, 149655, 138489, 144898, 127135, 143114, 147133, 132709, 139931, 138456, 134182, 124413, 140279, 135108, 146329, 127978, 136468, 124291, 142359, 129735, 131473, 138625, 151183, 124722, 139870, 142698, 140581, 142074, 148998, 142303, 132148, 129838, 142514, 150995, 140394, 133932, 142727, 149391, 143169, 128717, 143554, 149834, 148170, 146191, 139702, 147193, 133831, 131631, 130901, 138614, 147250, 159987, 173684, 156562);

-- trainingPathway should be set to N/A, 227 in total
UPDATE `ProgrammeMembership` SET `trainingPathway` = 'N/A' where `id` in (1072, 1084, 1064, 196587, 9060, 8776, 9135, 9048, 8823, 9009, 8975, 177167, 144291, 220397, 184172, 226818, 256777, 164798, 249201, 186907, 6633, 8990, 9087, 9022, 9046, 6609, 6632, 9058, 6623, 6627, 6610, 6625, 6619, 6615, 8963, 9133, 8796, 8944, 8982, 8864, 8881, 8768, 819, 810, 796, 242916, 822, 226464, 827, 799, 198597, 259235, 24964, 233691, 32958, 232939, 193220, 30396, 34861, 31582, 123554, 243375, 164252, 211338, 219339, 26402, 41918, 255282, 138114, 258203, 152095, 227627, 166091, 248559, 141026, 117104, 142576, 127120, 150820, 210009, 172895, 123110, 230955, 154202, 218102, 170596, 221101, 225181, 186700, 236976, 195267, 220566, 199244, 208316, 220164, 211258, 197818, 235530, 178768, 190171, 224623, 178047, 239883, 198119, 237080, 238082, 211505, 219927, 256502, 238815, 5660, 5630, 225450, 245227, 253693, 203388, 242982, 163859, 246668, 226953, 218239, 245487, 2382, 176311, 248696, 152382, 213304, 166485, 189336, 5855, 259545, 260855, 254042, 162463, 196004, 256172, 233984, 235705, 194804, 171874, 252412, 241943, 155358, 179632, 208343, 242119, 228702, 202483, 249183, 249280, 219891, 5306, 184954, 205732, 173006, 215844, 252658, 232620, 223292, 197617, 255697, 193885, 221304, 244733, 251896, 5759, 191822, 220664, 234860, 170950, 258533, 202534, 179228, 183044, 238086, 183359, 240818, 208089, 185256, 238407, 240218, 193135, 213603, 205999, 260944, 666, 622, 593, 637, 568, 571, 603, 564, 583, 615, 640, 625, 567, 592, 665, 588, 642, 662, 576, 650, 652, 660, 579, 655, 644, 594, 589, 657, 597, 612, 595, 584, 590, 591, 581, 630, 585, 624, 623, 604, 572, 601);
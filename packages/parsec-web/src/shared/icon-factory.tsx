import { Icon } from '@chakra-ui/react';
import type { ReactElement } from 'react';
import type { IconType } from 'react-icons';
import { AiFillEdit, AiFillHome, AiOutlineMenu } from 'react-icons/ai';
import { BsCircleFill, BsFillMoonStarsFill, BsFillSunFill } from 'react-icons/bs';
import { CgDockBottom, CgDockRight } from 'react-icons/cg';
import { GoChevronDown, GoChevronLeft, GoChevronRight, GoChevronUp, GoSearch, GoX } from 'react-icons/go';
import { GrStatusUnknown } from 'react-icons/gr';
import { ImBooks, ImHistory } from 'react-icons/im';
import { MdAdd } from 'react-icons/md';
import { SiGithub } from 'react-icons/si';
import { VscCheck, VscError, VscListTree, VscPlay, VscSettingsGear } from 'react-icons/vsc';

const icons = {
  chevronDown: GoChevronDown,
  chevronLeft: GoChevronLeft,
  chevronRight: GoChevronRight,
  chevronUp: GoChevronUp,
  circle: BsCircleFill,
  close: GoX,
  darkMode: BsFillMoonStarsFill,
  dockBottom: CgDockBottom,
  dockRight: CgDockRight,
  edit: AiFillEdit,
  execute: VscPlay,
  github: SiGithub,
  history: ImHistory,
  home: AiFillHome,
  invalid: VscError,
  lightMode: BsFillSunFill,
  menu: AiOutlineMenu,
  plus: MdAdd,
  queries: VscListTree,
  reference: ImBooks,
  search: GoSearch,
  settings: VscSettingsGear,
  unknown: GrStatusUnknown,
  valid: VscCheck
};

export type IconFactory = typeof icons;
export type IconFactoryKeys = keyof IconFactory;

export const iconFactory = (key: IconFactoryKeys): IconType => {
  if (icons[key]) {
    return icons[key];
  }

  // Fallback!
  console.error(`[ICON_FACTORY] Missing icon for ${key}!`);
  return GrStatusUnknown;
};

export const iconFactoryAs = (key: IconFactoryKeys, props = {}): ReactElement => {
  return <Icon as={iconFactory(key)} {...props} />;
};

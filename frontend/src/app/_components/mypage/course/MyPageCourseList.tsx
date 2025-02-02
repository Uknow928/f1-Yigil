'use client';
import { EventFor } from '@/types/type';
import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import FloatingActionButton from '../../FloatingActionButton';
import { TPopOverData } from '../../ui/popover/types';
import MyPageSelectBtns from '../MyPageSelectBtns';
import Pagination from '../Pagination';
import MyPageCourseItem from './MyPageCourseItem';
import UnLockIcon from '/public/icons/unlock.svg';
import TrashIcon from '/public/icons/trash.svg';
import LockIcon from '/public/icons/lock.svg';
import HamburgerIcon from '/public/icons/hamburger.svg';
import PlusIcon from '/public/icons/plus.svg';
import { TMyPageCourse } from '@/types/myPageResponse';
import { deleteMyCourse, getMyPageCourses } from '../hooks/myPageActions';
import Dialog from '../../ui/dialog/Dialog';

export default function MyPageCourseList({
  placeList,
  totalPage,
}: {
  placeList: TMyPageCourse[];
  totalPage: number;
}) {
  const [allCourseList, setAllCourseList] =
    useState<TMyPageCourse[]>(placeList);
  const [checkedList, setCheckedList] = useState<
    { course_id: TMyPageCourse['course_id']; is_private: boolean }[]
  >([]);

  const [currentPage, setCurrentPage] = useState<number>(1);
  const divideCount = 5;
  const [totalPageCount, setTotalPageCount] = useState(totalPage);

  const [selectOption, setSelectOption] = useState('all');
  const [sortOption, setSortOption] = useState<string>('desc');

  const [isDialogOpened, setIsDialogOpened] = useState(false);

  const [popOverData, setPopOverData] = useState<TPopOverData[]>([
    {
      label: '기록 나만보기',
      icon: <LockIcon className="w-6 h-6 stroke-black" />,
      onClick: () => onClickUnLock(),
    },
    {
      label: '기록 삭제하기',
      icon: <TrashIcon className="w-6 h-6" />,
      onClick: () => setIsDialogOpened(true),
    },
  ]);

  useEffect(() => {
    getCourse(currentPage, divideCount, sortOption, selectOption);
  }, [currentPage, sortOption, selectOption]);

  const getCourse = async (
    pageNum: number,
    size: number,
    sortOption: string,
    selectOption: string,
  ) => {
    const courseList = await getMyPageCourses(
      pageNum,
      size,
      sortOption,
      selectOption,
    );
    if (!courseList.success) {
      return setAllCourseList([]);
    }
    setTotalPageCount(courseList.data.total_pages);
    setAllCourseList([...courseList.data.content]);
  };

  useEffect(() => {
    if (selectOption === 'private') {
      setPopOverData([
        {
          label: '기록 공개하기',
          icon: <UnLockIcon className="w-6 h-6 stroke-black" />,
          onClick: () => onClickUnLock(),
        },
        {
          label: '기록 삭제하기',
          icon: <TrashIcon className="w-6 h-6" />,
          onClick: () => setIsDialogOpened(true),
        },
      ]);
    } else {
      setPopOverData([
        {
          label: '기록 나만보기',
          icon: <LockIcon className="w-6 h-6 stroke-black" />,
          onClick: () => onClickUnLock(),
        },
        {
          label: '기록 삭제하기',
          icon: <TrashIcon className="w-6 h-6" />,
          onClick: () => setIsDialogOpened(true),
        },
      ]);
    }
  }, [selectOption, checkedList]);

  useEffect(() => {
    setCurrentPage(1);
    getCourse(1, divideCount, sortOption, selectOption);
  }, [selectOption, sortOption]);

  const onClickDelete = (courseIds: number[]) => {
    // promise로 여러 코스 삭제를 한번에 처리하는 로직 필요
    // Promise.all(courseIds.map((course_id) => deleteMyCourse(course_id)));
  };
  // TODO: lock, unlock은 단일 id이냐 아니냐에 따라 다르게 호출
  const onClickUnLock = () => {};
  const onClickLock = () => {};

  const closeDialog = () => {
    setIsDialogOpened(false);
  };

  const onClickSelectOption = (option: string) => {
    setSelectOption(option);
    resetCheckList();
    setSortOption('desc');
  };

  const resetCourseList = () => {
    setAllCourseList([]);
  };
  const resetCheckList = () => {
    setCheckedList([]);
  };

  const onChangeSortOption = (option: string | number) => {
    if (typeof option === 'number') return;
    resetCourseList();
    setSortOption(option);
    resetCheckList();
    setCurrentPage(1);
    getCourse(1, divideCount, option, selectOption);
  };
  const onChangeAllCourse = (
    e: EventFor<'input', 'onChange'>,
    setIsChecked: Dispatch<SetStateAction<boolean>>,
  ) => {
    if (e.currentTarget.checked) {
      const allCourses = allCourseList.map((course) => {
        return { course_id: course.course_id, is_private: course.is_private };
      });
      setCheckedList(allCourses);
      setIsChecked(true);
    } else {
      resetCheckList();
      setIsChecked(false);
    }
  };

  const onChangeCheckedList = (
    course_id: TMyPageCourse['course_id'],
    is_private: boolean,
  ) => {
    if (!checkedList.length) setCheckedList([{ course_id, is_private }]);
    else {
      // checkList 배열의 각 값을 확인 후 값이 없으면 체크 리스트 추가 값이 있으면 filter로 제거
      const found = checkedList.find(
        (checked) => checked.course_id === course_id,
      );
      if (!found) {
        setCheckedList([...checkedList, { course_id, is_private }]);
      } else {
        const filteredList = checkedList.filter(
          (checkedId) => checkedId.course_id !== course_id,
        );
        setCheckedList(filteredList);
      }
    }
  };

  return (
    <>
      <div className="my-4 px-2">
        <MyPageSelectBtns
          selectOption={selectOption}
          sortOption={sortOption}
          onClickSelectOption={onClickSelectOption}
          onChangeSortOption={onChangeSortOption}
          onChangeAllList={onChangeAllCourse}
        />
      </div>
      {!!checkedList.length && (
        <div className="relative z-40">
          {isDialogOpened && (
            <Dialog
              text="기록을 삭제하시겠나요?"
              closeModal={closeDialog}
              handleConfirm={async () =>
                onClickDelete(checkedList.map((list) => list.course_id))
              }
            />
          )}
          <FloatingActionButton
            popOverData={popOverData}
            openedIcon={<PlusIcon className="rotate-45 duration-200 z-30" />}
            closedIcon={<HamburgerIcon className="w-20 h-20" />}
          />
        </div>
      )}
      {allCourseList.map(({ course_id, ...data }, idx) => (
        <MyPageCourseItem
          key={course_id}
          idx={idx}
          course_id={course_id}
          {...data}
          checkedList={checkedList}
          onChangeCheckedList={onChangeCheckedList}
          selectOption={selectOption}
        />
      ))}
      {!!allCourseList.length && (
        <Pagination
          currentPage={currentPage}
          setCurrentPage={setCurrentPage}
          totalPage={totalPageCount}
        />
      )}
    </>
  );
}

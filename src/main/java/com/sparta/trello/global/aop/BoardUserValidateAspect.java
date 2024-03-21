package com.sparta.trello.global.aop;

import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.global.security.UserDetailsImpl;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BoardUserValidateAspect {

    private final BoardUserJpaRepository boardUserJpaRepository;

    @Pointcut("execution(* com.sparta.trello.domain.board.service.*.*(..)) "
        + "&& !execution(* com.sparta.trello.domain.board.service.BoardService.createBoard(..))")
    private void boardTransaction() {
    }

    @Pointcut("execution(* com.sparta.trello.domain.card.service.*.*(..))")
    private void cardTransaction() {
    }

    @Pointcut("execution(* com.sparta.trello.domain.column.service.*.*(..))")
    private void columnTransaction() {
    }

    @Pointcut("execution(* com.sparta.trello.domain.comment.service.*.*(..))")
    private void commentTransaction() {
    }

    @Before("boardTransaction() || cardTransaction() || columnTransaction() || commentTransaction()")
    public void validateBoardUser(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        long boardId = 0L;

        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        if (!methodSignature.getMethod().getName()
            .equals("createBoard")) {
            String[] parameterNames = methodSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            if (args.length < 1) {
                log.info("No Parameter");
                return;
            } else {
                for (int i = 0; i < parameterNames.length; i++) {
                    if (parameterNames[i].equals("boardId")) {
                        boardId = (long) args[i];
                        break;
                    }
                }
            }

            workspaceUser(user, boardId);
        }
    }

    public void workspaceUser(User user, Long boardId) {
        BoardUser boardUser = boardUserJpaRepository.findByBoardIdAndUserId(boardId, user.getId());
        if (boardUser == null) {
            throw new NoSuchElementException("워크스페이스 권한이 없는 사용자입니다.");
        }
    }
}

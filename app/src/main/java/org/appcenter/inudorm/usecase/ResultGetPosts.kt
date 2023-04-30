package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Post
import kotlin.reflect.KSuspendFunction2


private val postRepos: Map<BoardType, KSuspendFunction2<Dorm, Int, ArrayList<Post>>> =
    mapOf(
        BoardType.Top to communityRepository::getTopPostsByDorm,
        BoardType.Regular to communityRepository::getPostsByDorm
    )

class ResultGetPosts : ResultUseCase<GetPostParams, ArrayList<Post>>() {
    override suspend fun onExecute(params: GetPostParams): ArrayList<Post> {
        return postRepos[params.boardType]?.invoke(params.dorm, params.page) ?: ArrayList()
    }
}
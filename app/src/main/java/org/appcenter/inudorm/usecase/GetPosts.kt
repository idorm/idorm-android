package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.App.Companion.communityRepository
import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Post
import kotlin.reflect.KSuspendFunction2

sealed class BoardType {
    object Top : BoardType()
    object Regular : BoardType()
}

data class GetPostParams(val boardType: BoardType, val dorm: Dorm, val page: Int)

private val postRepos: Map<BoardType, KSuspendFunction2<Dorm, Int, ArrayList<Post>>> =
    mapOf(
        BoardType.Top to communityRepository::getTopPostsByDorm,
        BoardType.Regular to communityRepository::getPostsByDorm
    )

class GetPosts : UseCase<GetPostParams, ArrayList<Post>>() {
    override suspend fun onExecute(params: GetPostParams): ArrayList<Post> {
        return postRepos[params.boardType]?.invoke(params.dorm, params.page) ?: ArrayList()
    }
}
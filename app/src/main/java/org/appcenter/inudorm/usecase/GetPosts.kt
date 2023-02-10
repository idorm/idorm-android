package org.appcenter.inudorm.usecase

import org.appcenter.inudorm.model.Dorm
import org.appcenter.inudorm.model.board.Post
import org.appcenter.inudorm.repository.CommunityRepository
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction3

sealed class BoardType {
    object Top : BoardType()
    object Regular : BoardType()
}

data class GetPostParams(val boardType: BoardType, val dorm: Dorm, val page: Int)

private val postRepos: Map<BoardType, KSuspendFunction3<CommunityRepository, Dorm, Int, ArrayList<Post>>> =
    mapOf(
        BoardType.Top to CommunityRepository::getTopPostsByDorm,
        BoardType.Regular to CommunityRepository::getPostsByDorm
    )

class GetPosts (private val communityRepository: CommunityRepository) :
    UseCase<GetPostParams, ArrayList<Post>>() {
    override suspend fun onExecute(params: GetPostParams): ArrayList<Post> {
        return postRepos[params.boardType]?.invoke(communityRepository, params.dorm, params.page)
            ?: ArrayList()
    }
}